package eum.backed.server.commumityapi.service;

import com.google.firebase.auth.FirebaseToken;
import eum.backed.server.commumityapi.controller.dto.request.UsersRequestDTO;
import eum.backed.server.commumityapi.domain.user.Users;
import eum.backed.server.commumityapi.domain.user.UsersRepository;
import eum.backed.server.enums.Authority;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.build.CachedReturnPlugin;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUsersDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return usersRepository.findByEmail(username)
//                .map(this::createUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        return usersRepository.findByEmail(email).map(this::createUserDetails).orElse(Users.builder().build());
    }

    // 해당하는 Users 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Users users) {
        return new User(users.getEmail(), "", users.getAuthorities());
    }
    @Transactional
    public Users create(UsersRequestDTO.SignUp signUp, FirebaseToken decode) {
        Users users = Users.builder()
                .email(decode.getEmail())
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
        return users;
    }

}