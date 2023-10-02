package eum.backed.server.commumityapi.service;

import eum.backed.server.commumityapi.domain.user.Users;
import eum.backed.server.commumityapi.domain.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUsersDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return usersRepository.findByEmail(username)
//                .map(this::createUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        return usersRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("해당 유저가 없습니다"));
    }

    // 해당하는 Users 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Users users) {
        return new User(users.getUsername(), users.getPassword(), users.getAuthorities());
    }
}