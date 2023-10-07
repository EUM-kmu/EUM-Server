package eum.backed.server.commumityapi.service;

import com.google.firebase.auth.FirebaseToken;
import eum.backed.server.commumityapi.controller.dto.request.UsersRequestDTO;
import eum.backed.server.commumityapi.controller.dto.Response;
import eum.backed.server.commumityapi.controller.dto.response.UsersResponseDTO;
import eum.backed.server.commumityapi.domain.user.Users;
import eum.backed.server.commumityapi.domain.user.UsersRepository;
import eum.backed.server.enums.Authority;
import eum.backed.server.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;
    private final Response response;

    public ResponseEntity<?> signUp(UsersRequestDTO.SignUp signUp){
        if(usersRepository.existsByEmail(signUp.getEmail())){
            return response.fail("이미 있는 이메일입니다", HttpStatus.BAD_REQUEST);
        }
        Users users = Users.builder()
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .introduction(signUp.getIntroduction())
                .name(signUp.getName())
                .sex(signUp.getSex())
                .birth(signUp.getBirth())
                .nickname(signUp.getNickname())
                .address(signUp.getAddress())
                .phone(signUp.getPhone())
                .isBanned(false)
                .authorities(Collections.singletonList(Authority.ROLE_USER.name()))
                .totalVolunteerTime(0).build();
        usersRepository.save(users);
        return response.success("회원가입에 성공");
    }

    public ResponseEntity<?> signIn(UsersRequestDTO.SignIn signIn) {
        UsernamePasswordAuthenticationToken authenticationToken = signIn.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        UsersResponseDTO.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "로그인에 성공했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> reissue(UsersRequestDTO.Reissue reissue) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        if(!refreshToken.equals(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 4. 새로운 토큰 생성
        UsersResponseDTO.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
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

    public ResponseEntity<?> register(UsersRequestDTO.Test test, FirebaseToken decodedToken) {
        if(usersRepository.existsByEmail(decodedToken.getEmail())) {
            UsersResponseDTO.TokenInfo tokenInfo = jwtTokenProvider.generateToken(decodedToken.getEmail());
            // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            redisTemplate.opsForValue()
                    .set("RT:" +decodedToken.getEmail(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

            return response.success(tokenInfo, "로그인에 성공했습니다.", HttpStatus.OK);
        }
            Users users = Users.builder()
                    .email(decodedToken.getEmail())
                    .introduction(test.getIntroduction())
                    .name(test.getName())
                    .sex(test.getSex())
                    .birth(test.getBirth())
                    .nickname(test.getNickname())
                    .address(test.getAddress())
                    .phone(test.getPhone())
                    .isBanned(false)
                    .authorities(Collections.singletonList(Authority.ROLE_USER.name()))
                    .totalVolunteerTime(0).build();
            usersRepository.save(users);
            return response.success("회원가입");

    }
    public ResponseEntity<?> authsignin(FirebaseToken decodedToken) {
        if(usersRepository.existsByEmail(decodedToken.getEmail())) {
            UsersResponseDTO.TokenInfo tokenInfo = jwtTokenProvider.generateToken(decodedToken.getEmail());
            // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            redisTemplate.opsForValue()
                    .set("RT:" +decodedToken.getEmail(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

            return response.success(tokenInfo, "로그인에 성공했습니다.", HttpStatus.OK);
        }
        return response.success("회원정보가 없습니다. 회원 요청으로 넘어가주세요");
    }
    public UsernamePasswordAuthenticationToken toAuthentication(String email){
        return new UsernamePasswordAuthenticationToken(email, "");
    }
}
