package eum.backed.server.controller.community.dto.request;

import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.community.marketpost.Slot;
import eum.backed.server.domain.community.marketpost.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

public class PostRequestDTO {

    @Setter
    @Getter
    public static class MarketCreate {
        @NotEmpty(message = "제목을 입력하세요")
        private String title;
        @NotEmpty(message = "내용을 입력하세요")
        private String content;
        //        마감시간은 없애고, 시간은 오전, 오후, 상관없음.

        private String startTime;
        @NotNull
        private Slot slot;
        @NotEmpty(message = "상세 주소를 작성해주세요")
        private String location;

        @Positive(message = "참여 시간은 양수여야 합니다")
        private int volunteerTime;
        @NotNull(message = "null이 오면 안됩니다")
        private MarketType marketType;

        @Min(value = 1, message = "최소값은 1이어야 합니다.")
        @Max(value = 50, message = "최대값은 50이어야 합니다.")
        private int maxNumOfPeople;

        @NotNull(message = "카테고리를 입력해주세요")
        private String category;
    }
    @Setter
    @Getter
    public static class MarketUpdate {
        @NotEmpty(message = "제목을 입력하세요")
        private String title;
        @NotEmpty(message = "내용을 입력하세요")
        private String content;
        @NotNull(message = "null이 오면 안됩니다")
        private Slot slot;
        private String startDate;
        @NotEmpty(message = "상세 주소를 작성해주세요")
        private String location;
        @Min(value = 1, message = "최소값은 1이어야 합니다.")
        @Max(value = 50, message = "최대값은 50이어야 합니다.")
        private int maxNumOfPeople;

        @Positive(message = "참여시간은 양수여야함")
        private int volunteerTime;

    }
    @Getter
    @Setter
    public static class UpdateStatus{
        private Status status;
    }
}
