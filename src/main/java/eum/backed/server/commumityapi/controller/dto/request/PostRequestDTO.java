package eum.backed.server.commumityapi.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class PostRequestDTO {

    @Setter
    @Getter
    public static class Create{
        @NotEmpty(message = "제목을 입력하세요")
        private String title;
        @NotEmpty(message = "내용을 입력하세요")
        private String content;
        private String startTime;
        private String endTime;
        @NotEmpty(message = "금액을 입력하세요;")
        private int pay;
        private String location;
        private int volunteerTime;
        private boolean isHelper;
        private int maxNumOfPeople;
        @NotEmpty(message = "카테고리를 선택")
        private Long categoryId;
        private Long guId;
    }
    @Setter
    @Getter
    public static class Update{
        @NotEmpty
        private Long postId;
        @NotEmpty(message = "제목을 입력하세요")
        private String title;
        @NotEmpty(message = "내용을 입력하세요")
        private String content;
//        private String startTime;
//        private String endTime;
//        @NotEmpty(message = "금액을 입력하세요;")
//        private int pay;
//        private String location;
//        private int volunteerTime;
//        private int maxNumOfPeople;
//        @NotEmpty(message = "카테고리를 선택")
//        private Long categoryId;
//        private Long guId;
    }
}
