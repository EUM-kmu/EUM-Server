package eum.backed.server.controller.community.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class VotePostRequestDTO {
    @Setter
    @Getter
    public static class Create{
        private String title;
        private String content;
        private String endDate;
    }
    @Getter
    @Setter
    public static class Update {
        private Long votePostId;
        private String title;
        private String content;
        private String endDate;
    }
}
