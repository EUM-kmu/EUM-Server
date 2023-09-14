package eum.backed.server.service;

import eum.backed.server.controller.dto.request.UsersRequestDTO;
import eum.backed.server.controller.dto.Response;
import eum.backed.server.controller.dto.response.UsersResponseDTO;
import eum.backed.server.domain.user.Users;
import eum.backed.server.domain.user.UsersRepository;
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
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
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

}
