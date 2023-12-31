package eum.backed.server.config.jwt;

import eum.backed.server.controller.community.dto.response.UsersResponseDTO;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.enums.Authority;
import eum.backed.server.exception.TokenException;
import eum.backed.server.service.community.CustomUsersDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 12 * 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일
    @Autowired
    private CustomUsersDetailsService customUsersDetailsService;
    private final Key key;
    @Autowired
    private UsersRepository usersRepository;
    @Value("${jwt.secret}")
    private String jwtKey;

    public JwtTokenProvider(){
        byte[] keyBytes = Decoders.BASE64.decode("VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHN");
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public UsersResponseDTO.TokenInfo generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return UsersResponseDTO.TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }
    public UsersResponseDTO.TokenInfo generateToken(String email, Role role) {
        // Check if the user has the TEST role
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid user"));
        if (getUser.getRole() == Role.TEST) {
            // Set the access token expiration time to infinity for TEST users
            long now = (new Date()).getTime();
            Date accessTokenExpiresIn = new Date(Long.MAX_VALUE);

            // Generate the access token
            String accessToken = Jwts.builder()
                    .setSubject(email)
                    .claim(AUTHORITIES_KEY, "authorities")
                    .setExpiration(accessTokenExpiresIn)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            // Generate the refresh token
            String refreshToken = Jwts.builder()
                    .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            return UsersResponseDTO.TokenInfo.builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                    .role(role)
                    .build();
        } else {
            // Generate the access and refresh tokens normally for non-TEST users
            long now = (new Date()).getTime();
            Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

            // Generate the access token
            String accessToken = Jwts.builder()
                    .setSubject(email)
                    .claim(AUTHORITIES_KEY, "authorities")
                    .setExpiration(accessTokenExpiresIn)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            // Generate the refresh token
            String refreshToken = Jwts.builder()
                    .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            return UsersResponseDTO.TokenInfo.builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                    .role(role)
                    .build();
        }
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
//        if (claims.get(AUTHORITIES_KEY) == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }
//
//        // 클레임에서 권한 정보 가져오기
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
        UserDetails userDetails = customUsersDetailsService.loadUserByUsername(claims.getSubject());
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), accessToken, userDetails.getAuthorities());
    }


    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) throws SecurityException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            // 401 Unauthorized
            throw e;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            // 401 Expired
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            // 400 Bad Request
            throw e;
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            // 400 Bad Request
            throw e;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }catch (SignatureException e){
            throw new TokenException("Invalid JWT Token");
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

}


