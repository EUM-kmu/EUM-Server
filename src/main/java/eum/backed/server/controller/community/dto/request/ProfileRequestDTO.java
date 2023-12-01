package eum.backed.server.controller.community.dto.request;

import eum.backed.server.domain.community.avatar.AvatarName;
import lombok.Getter;
import lombok.Setter;

import javax.naming.Name;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class ProfileRequestDTO {
    @Getter
    @Setter
    public static class CreateProfile{
        @NotEmpty(message = "닉네임을 입력하세요")
        private String nickname;
        private String introduction;
        @Positive(message = "지역id 입력해주세요")
        private Long regionId;

        @NotNull(message = "캐릭터를 선택하세요")
        private AvatarName avatarName;

    }
    @Getter
    @Setter
    public static class UpdateProfile{
        @NotEmpty(message = "닉네임을 입력하세요")
        private String nickname;
        private String introduction;
        @Positive(message = "지역id 입력해주세요")
        private Long regionId;
        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "\\d{4}", message = "비밀번호는 4자리 숫자여야 합니다.")
        private String accountPassword;
        @NotNull(message = "캐릭터를 선택하세요")
        private AvatarName avatarName;

    }

}
