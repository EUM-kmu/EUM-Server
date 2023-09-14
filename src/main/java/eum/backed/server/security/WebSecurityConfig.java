package eum.backed.server.security;


import eum.backed.server.jwt.JwtAuthenticationFilter;
import eum.backed.server.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers("/user/signup", "user/sigin", "/user/authority", "/api/v1/users/reissue", "/api/v1/users/logout").permitAll()
                .antMatchers("/user/userTest").hasRole("USER")
                .antMatchers("/user/adminTest").hasRole("ADMIN")
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
