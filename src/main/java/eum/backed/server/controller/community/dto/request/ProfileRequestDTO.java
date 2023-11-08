package eum.backed.server.controller.community.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

public class ProfileRequestDTO {
    @Getter
    @Setter
    public static class CreateProfile{
        @NotEmpty
        private String nickname;
        private String introduction;
        private String dong;
        private String accountPassword;
        private String avatar;


    }

}
