package eum.backed.server.controller.community.DTO.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


public class CommentRequestDTO {
    @Setter
    @Getter
    public static class CommentCreate {
        @NotEmpty(message = "댓글을 입력해야합니다")
        private String content;
    }

    @Getter
    @Setter
    public static class CommentUpdate {
        @NotEmpty(message = "댓글을 입력해야합니다")
        private String content;

    }

}
