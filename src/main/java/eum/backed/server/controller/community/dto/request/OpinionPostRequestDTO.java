package eum.backed.server.controller.community.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

public class OpinionPostRequestDTO {
    @Getter
    @Setter
    public static class Create{
        private String title;
        private String content;
    }
    @Getter
    @Setter
    public static class Update {
        private Long opinionPostId;
        private String title;
        private String content;
    }
}
