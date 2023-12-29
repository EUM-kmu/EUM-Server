package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.config.jwt.JwtTokenProvider;
import eum.backed.server.controller.community.DTO.request.UsersRequestDTO;
import eum.backed.server.controller.community.DTO.response.UsersResponseDTO;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.SocialType;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.domain.community.withdrawalcategory.WithdrawalCategory;
import eum.backed.server.domain.community.withdrawalcategory.WithdrawalCategoryRepository;
import eum.backed.server.domain.community.withdrawaluser.WithdrawalUser;
import eum.backed.server.domain.community.withdrawaluser.WithdrawalUserRepository;
import eum.backed.server.enums.Authority;
import eum.backed.server.exception.TokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final WithdrawalCategoryRepository withdrawalCategoryRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final WithdrawalUserRepository withdrawalUserRepository;


    public APIResponse signUp(UsersRequestDTO.SignUp signUp){
        if(usersRepository.existsByEmail(signUp.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
        }
        Users users = Users.builder()
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .isBanned(false)
                .isDeleted(false)
                .role(Role.ROLE_UNPROFILE_USER)
                .socialType(SocialType.SELF)
                .authorities(Collections.singletonList(Authority.ROLE_USER.name())).build();
        usersRepository.save(users);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS,"자체 회원가입 성공");
    }

    public APIResponse<UsersResponseDTO.TokenInfo> signIn(UsersRequestDTO.SignIn signIn) {
        Users getUser = usersRepository.findByEmail(signIn.getEmail()).orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 정보"));
        if(!passwordEncoder.matches(signIn.getPassword(),getUser.getPassword())) throw new IllegalArgumentException("잘못된 비밀번호");
        UsersResponseDTO.TokenInfo  tokenInfo = jwtTokenProvider.generateToken(getUser.getEmail(),getUser.getRole());


        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + getUser.getEmail(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return APIResponse.of(SuccessCode.SELECT_SUCCESS,tokenInfo);
    }

    public APIResponse<UsersResponseDTO.TokenInfo> reissue(UsersRequestDTO.Reissue reissue) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            throw new TokenException("Refresh Token 정보가 유효하지 않습니다.");
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            throw new TokenException("Redis 에 RefreshToken 이 존재하지 않습니다");
        }
        if(!refreshToken.equals(reissue.getRefreshToken())) {
            throw new TokenException("refresh정보가 일치 하지 않습니다");
        }

        // 4. 새로운 토큰 생성
        UsersResponseDTO.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return APIResponse.of(SuccessCode.UPDATE_SUCCESS,tokenInfo);
    }

    public void logout(String token) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(token)) {
            throw new TokenException("잘못된 토큰 입니다");
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(token);
        redisTemplate.opsForValue()
                .set(token, "logout", expiration, TimeUnit.MILLISECONDS);

    }

    public APIResponse<UsersResponseDTO.TokenInfo> getToken(String email, String uid, SocialType socialType){
        UsersResponseDTO.TokenInfo tokenInfo = null;
        if(email.isBlank()) throw new IllegalArgumentException("email is empty");
        Role role;
        if(usersRepository.existsByEmail(email)){
            if(usersRepository.existsByEmailAndRole(email,Role.ROLE_USER)){
                role = Role.ROLE_USER;
                tokenInfo = jwtTokenProvider.generateToken(email,role);
//                return new DataResponse<>(tokenInfo).success(tokenInfo, "로그인 성공");
            } else if (usersRepository.existsByEmailAndRole(email,Role.ROLE_UNPROFILE_USER) ||usersRepository.existsByEmailAndRole(email,Role.ROLE_UNPASSWORD_USER )) {
                role = Role.ROLE_UNPROFILE_USER;
                tokenInfo = jwtTokenProvider.generateToken(email,role);
            }
        }else{
            role = Role.ROLE_UNPROFILE_USER;
            Users temporaryUser = Users.builder().email(email).role(role).uid(uid).isDeleted(false).isBanned(false
            ).socialType(socialType).build();
            usersRepository.save(temporaryUser);
            tokenInfo = jwtTokenProvider.generateToken(email,role);
        }
        redisTemplate.opsForValue()
                .set("RT:" +email, tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, tokenInfo);
    }

    public APIResponse<UsersResponseDTO.UserRole> validateToken(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid email"));
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, UsersResponseDTO.UserRole.builder().role(getUser.getRole()).build());
    }

    public void withdrawal(UsersRequestDTO.Withdrawal withdrawal, Users user) {
        WithdrawalCategory withdrawalCategory = withdrawalCategoryRepository.findById(withdrawal.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("초기 카테고리 데이터 미설정"));
        WithdrawalUser withdrawalUser = WithdrawalUser.toEntity(user,withdrawal.getReason(),withdrawalCategory);
        withdrawalUserRepository.save(withdrawalUser);
        user.removeEmail();
        user.setDeleted();
        usersRepository.save(user);
    }
    public Users findByEmail(String email){
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid email"));
        return getUser;
    }
    public Users findById(Long userId){
        Users getUser = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid email"));
        return getUser;
    }
}
