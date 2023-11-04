package eum.backed.server.controller.community.dto.response;

import lombok.Getter;
import lombok.Setter;

public class VotePostResponseDTO {
    @Getter
    @Setter
    public static class VotePostResponses{
        private Long userId;
        private String userNickName;
        private String userAddress;
        private String customCreatedTime;
        private Float agreePercentage;
        private Float disagreePercentage;
        private String title;
        private String content;
        private String endDate;
        private Boolean isVoting;
        private int commentCount;



    }
}
