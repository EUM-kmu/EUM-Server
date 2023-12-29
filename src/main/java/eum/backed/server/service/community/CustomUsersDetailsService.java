package eum.backed.server.service.community;

import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
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
        return usersRepository.findByEmail(email).map(this::createUserDetails).orElse(Users.builder().build());
    }

    // 해당하는 Users 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Users users) {
        return new User(users.getEmail(), "", users.getAuthorities());
    }
//    @Transactional
//    public Users create(UsersRequestDTO.SignUp signUp, FirebaseToken decode) {
//        Users users = Users.builder()
//                .email(decode.getEmail())
//                .introduction(signUp.getIntroduction())
//                .name(signUp.getName())
//                .sex(signUp.getSex())
//                .birth(signUp.getBirth())
//                .nickname(signUp.getNickname())
//                .address(signUp.getAddress())
//                .phone(signUp.getPhone())
//                .banned(false)
//                .authorities(Collections.singletonList(Authority.ROLE_USER.name()))
//                .totalVolunteerTime(0).build();
//        usersRepository.save(users);
//        return users;
//    }

}