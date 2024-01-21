package eum.backed.server.controller.community.DTO.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class ProfileRequestDTO {
    @Getter
    @Setter
    public static class CreateProfile{
        @NotEmpty(message = "닉네임을 입력하세요")
        private String nickname;

        private Long avatarId;

    }
    @Getter
    @Setter
    public static class UpdateProfile{
        @NotEmpty(message = "닉네임을 입력하세요")
        private String nickname;
        private Long avatarId;

    }

}
