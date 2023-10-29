package eum.backed.server.controller.community.dto.request;

import eum.backed.server.domain.community.profile.Avatar;
import lombok.Builder;
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
        private String dong;
        private Avatar avatar;
        private String accountPassword;

    }

}
