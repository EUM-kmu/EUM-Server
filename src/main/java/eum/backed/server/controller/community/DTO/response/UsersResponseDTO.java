package eum.backed.server.controller.community.DTO.response;

import eum.backed.server.domain.community.user.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UsersResponseDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @ApiModel(value = "로그인 시 제공되는 데이터")
    public static class TokenInfo {
        @ApiModelProperty(value = "토큰 타입")
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
