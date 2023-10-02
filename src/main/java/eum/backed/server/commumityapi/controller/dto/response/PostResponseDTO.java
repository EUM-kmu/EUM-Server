package eum.backed.server.commumityapi.controller.dto.response;

import eum.backed.server.commumityapi.domain.post.Status;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class PostResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @ApiModel(value = "전체 데이터 정렬")
    public static class PostResponse {
        private Long postId;
        private String title;
        private String content;
        private String startDate;
        private String endDate;
        private int pay;
        private String location;
        private int volunteerTime;
        private boolean isHelper;
        private int maxNumOfPeople;
        private String category;
        private Status status;
    }
}
