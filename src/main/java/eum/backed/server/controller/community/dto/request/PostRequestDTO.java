package eum.backed.server.controller.community.dto.request;

import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.community.marketpost.Slot;
import eum.backed.server.domain.community.marketpost.Status;
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
        //        마감시간은 없애고, 시간은 오전, 오후, 상관없음.
        private String startTime;
        private Slot slot;
        @NotEmpty(message = "금액을 입력하세요;")
        private String location;
        private int volunteerTime;
        private MarketType marketType;
        private int maxNumOfPeople;
        @NotEmpty(message = "카테고리를 선택")
        private Long categoryId;
        private String dong;
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


//        @NotEmpty(message = "금액을 입력하세요;")
        private Slot slot;
        private String startDate;
        private String location;
        private int providingHelp;
        private int maxNumOfPeople;
        private String dong;
        private Status status;
//        @NotEmpty(message = "카테고리를 선택")
//        private Long categoryId;
//        private Long guId;
    }
}
