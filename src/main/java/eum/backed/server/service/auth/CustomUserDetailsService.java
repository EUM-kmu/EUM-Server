package eum.backed.server.service.auth;

import eum.backed.server.domain.auth.CustomUserDetails;
import eum.backed.server.domain.auth.dto.CustomUserInfoDto;
import eum.backed.server.domain.auth.user.Users;
import eum.backed.server.domain.auth.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Users user = usersRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없다"));
        CustomUserInfoDto userInfoDto = modelMapper.map(user, CustomUserInfoDto.class);
        log.info(userInfoDto.getRole().toString());
        return new CustomUserDetails(userInfoDto);
    }
}
