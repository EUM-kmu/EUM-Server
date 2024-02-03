package eum.backed.server.controller.community.DTO.response;

import eum.backed.server.common.KoreaLocalDateTime;
import eum.backed.server.controller.community.DTO.request.enums.MarketType;
import eum.backed.server.domain.auth.user.Users;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.Slot;
import eum.backed.server.domain.community.marketpost.Status;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MarketPostResponseDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class MarketPostResponse{
        private Long postId;
        private String title;
        private String content;
        private String  createdDate;
        private Status status;
        private String startDate;
        private Slot slot;
        private String location;
        private Long pay;
        private int volunteerTime;
        private MarketType marketType;
        private int currentApplicant;
        private int maxNumOfPeople;
        private String category;
        private int commentCount;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    public static class MarketPostWithComment {
        private ProfileResponseDTO.UserInfo writerInfo;
        private UserCurrentStatus userCurrentStatus;
        private MarketPostResponse marketPostResponse;
        private List<CommentResponseDTO.CommentResponse> commentResponses;
    }
    @Builder
    @Getter
    @Setter
    private static class UserCurrentStatus{
        private Boolean isWriter;
        private Boolean isApplicant;
        private Boolean isScrap;
        private eum.backed.server.domain.community.apply.Status applyStatus;
    }
    public static MarketPostResponseDTO.MarketPostResponse toMarketPostResponse(MarketPost marketPost, int commentCount,int currentApplicant){
        String createdTime = KoreaLocalDateTime.localDateTimeToKoreaZoned(marketPost.getCreateDate());
        String startTime = KoreaLocalDateTime.dateToKoreaZone(marketPost.getStartDate());
        return MarketPostResponse.builder()
                .postId(marketPost.getMarketPostId())
                .title(marketPost.getTitle())
                .createdDate(createdTime)
                .startDate(startTime)
                .slot(marketPost.getSlot())
                .content(marketPost.getContent())
                .pay(marketPost.getPay())
                .volunteerTime(marketPost.getVolunteerTime())
                .marketType(marketPost.getMarketType())
                .location(marketPost.getLocation())
                .category(marketPost.getMarketCategory().getContents())
                .status(marketPost.getStatus())
                .commentCount(commentCount)
                .currentApplicant(currentApplicant)
                .maxNumOfPeople(marketPost.getMaxNumOfPeople())
                .build();
    }
    public static MarketPostWithComment toMarketPostWithComment(Users user, MarketPost marketPost, List<CommentResponseDTO.CommentResponse> commentResponses, Boolean isApply, Boolean isScrap, eum.backed.server.domain.community.apply.Status tradingStatus){
        UserCurrentStatus userCurrentStatus = UserCurrentStatus.builder().isApplicant(isApply).isScrap(isScrap).isWriter(user==marketPost.getUser()).applyStatus(tradingStatus).build();
        MarketPostResponse marketPostResponse = toMarketPostResponse(marketPost, commentResponses.size(), marketPost.getApplies().size());
        return MarketPostWithComment.builder()
                .writerInfo(ProfileResponseDTO.toUserInfo(marketPost.getUser()))
                .userCurrentStatus(userCurrentStatus)
                .marketPostResponse(marketPostResponse)
                .commentResponses(commentResponses)
                .build();
    }
}
