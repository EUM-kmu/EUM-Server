package eum.backed.server.controller.community.dto.request;

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
//        마감시간은 없애고, 시간은 오전, 오후, 상관없음.
        private String endTime;
        @NotEmpty(message = "금액을 입력하세요;")
        private int pay;
        private String location;
        private int volunteerTime;
        private boolean needHelper;
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
        private String startTime;
//        private String endTime;
//        @NotEmpty(message = "금액을 입력하세요;")
        private int pay; //undefineE도 있음,,
        private String location;
        private int volunteerTime;
        private int maxNumOfPeople;
//        @NotEmpty(message = "카테고리를 선택")
//        private Long categoryId;
//        private Long guId;
    }
}
