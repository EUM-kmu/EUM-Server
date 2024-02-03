package eum.backed.server.controller.community.DTO.response;

import eum.backed.server.domain.auth.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UsersResponseDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
        private Role role;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    public static class UserRole {
        private Role role;

    }



}
