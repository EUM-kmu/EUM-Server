package eum.backed.server.domain.auth.dto;

import eum.backed.server.domain.community.user.Role;
import lombok.*;
import org.checkerframework.checker.units.qual.N;

@Getter
@Builder
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomUserInfoDto {
    Long userId;
    String uid;
    String email;
    String password;
    boolean isBanned;
    Role role;
    boolean isDeleted;

}
