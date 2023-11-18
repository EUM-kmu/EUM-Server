package eum.backed.server.controller.community.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class CommentRequestDTO {
    @Setter
    @Getter
    public static class Create{
        @NotEmpty
        private String content;
    }

    @Getter
    @Setter
    public static class Update{
        private String content;

    }

}
