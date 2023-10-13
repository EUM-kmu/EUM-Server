package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.config.jwt.JwtTokenProvider;
import eum.backed.server.controller.community.dto.Response;
import eum.backed.server.controller.community.dto.request.UsersRequestDTO;
import eum.backed.server.controller.community.dto.response.UsersResponseDTO;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.enums.Authority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;
    private final Response response;
    private final DataResponse dataResponse;


    public DataResponse signUp(UsersRequestDTO.SignUp signUp){
        if(usersRepository.existsByEmail(signUp.getEmail())){
            return dataResponse.fail("이미 있는 이메일입니다", HttpStatus.BAD_REQUEST);
        }
        Users users = Users.builder()
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .banned(false)
                .role(Role.ROLE_TEMPORARY_USER)
                .authorities(Collections.singletonList(Authority.ROLE_TEMPORARY_USER.name())).build();
        usersRepository.save(users);
        return dataResponse.success("자체 회원가입에 성공");
    }

    public DataResponse<UsersResponseDTO.TokenInfo> signIn(UsersRequestDTO.SignIn signIn) {
        Users getUser = usersRepository.findByEmail(signIn.getEmail()).orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 정보"));
        if(!passwordEncoder.matches(signIn.getPassword(),getUser.getPassword())) throw new IllegalArgumentException("잘못된 비밀번호");
        UsersResponseDTO.TokenInfo  tokenInfo = jwtTokenProvider.generateToken(getUser.getEmail(),getUser.getRole());


        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + getUser.getEmail(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return dataResponse.success(tokenInfo, "로그인에 성공했습니다.", HttpStatus.OK);
    }

    public DataResponse<UsersResponseDTO.TokenInfo> reissue(UsersRequestDTO.Reissue reissue) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return dataResponse.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            return dataResponse.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        if(!refreshToken.equals(reissue.getRefreshToken())) {
            return dataResponse.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 4. 새로운 토큰 생성
        UsersResponseDTO.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return dataResponse.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> logout(UsersRequestDTO.Logout logout) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }
    //엑세스 토큰으로 프로필 만들기
    public DataResponse register(UsersRequestDTO.AuthSignup authSignup, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("로그인되지 않은 유저"));
        getUser.setIntroduction(authSignup.getIntroduction());
        getUser.setName(authSignup.getName());
        getUser.setNickname(authSignup.getNickname());
        getUser.setAddress(authSignup.getAddress());
        getUser.setBanned(false);
        getUser.setTotalVolunteerTime(0);
        getUser.setRole(Role.ROLE_USER);
        usersRepository.save(getUser);
        return dataResponse.success("회원가입");

    }
    public DataResponse<UsersResponseDTO.TokenInfo> getToken(String email){
        UsersResponseDTO.TokenInfo tokenInfo = null;
        if(email.isBlank()) throw new IllegalArgumentException("email is empty");
        Role role = null;
        if(usersRepository.existsByEmail(email)){
            if(usersRepository.existsByEmailAndRole(email,Role.ROLE_USER)){
                role = Role.ROLE_USER;
                tokenInfo = jwtTokenProvider.generateToken(email,role);
//                return new DataResponse<>(tokenInfo).success(tokenInfo, "로그인 성공");
            } else if (usersRepository.existsByEmailAndRole(email,Role.ROLE_TEMPORARY_USER)) {
                role = Role.ROLE_TEMPORARY_USER;
                tokenInfo = jwtTokenProvider.generateToken(email,role);
            }
        }else{
            role = Role.ROLE_TEMPORARY_USER;
            Users temporaryUser = Users.builder().email(email).role(role).build();
            usersRepository.save(temporaryUser);
            tokenInfo = jwtTokenProvider.generateToken(email,role);
        }
        redisTemplate.opsForValue()
                .set("RT:" +email, tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        return dataResponse.success(tokenInfo, role.toString());
    }
}
