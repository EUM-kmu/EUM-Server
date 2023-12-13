package eum.backed.server.controller.community.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.NotEmpty;

public class OpinionPostRequestDTO {
    @Getter
    @Setter
    public static class Create{
        @NotEmpty(message = "제목을 입력해주세요")
        private String title;
        @NotEmpty(message = "내용을 입력해주세요")
        private String content;
    }
    @Getter
    @Setter
    public static class Update {
        @NotEmpty(message = "제목을 입력해주세요")
        private String title;
        @NotEmpty(message = "내용을 입력해주세요")
        private String content;
    }
}
