package eum.backed.server.controller.community.dto.request;

import eum.backed.server.domain.community.avatar.AvatarName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class ProfileRequestDTO {
    @Getter
    @Setter
    public static class CreateProfile{
        @NotEmpty
        private String nickname;
        private String introduction;
        private String townShip;
        private String accountPassword;
        private AvatarName avatarName;

    }
    @Getter
    @Setter
    public static class UpdateProfile{
        @NotEmpty
        private String nickname;
        private String introduction;
        private String townShip;
        private String accountPassword;
        private AvatarName avatarName;

    }

}
