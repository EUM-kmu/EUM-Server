package eum.backed.server.controller.community.DTO.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


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
