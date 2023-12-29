package eum.backed.server.controller.community.DTO.response;

import eum.backed.server.common.KoreaLocalDateTime;
import eum.backed.server.controller.community.DTO.request.enums.MarketType;
import eum.backed.server.domain.community.marketpost.Slot;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.Status;
import eum.backed.server.domain.community.user.Users;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostResponseDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class MarketPostResponse{
        private Long postId;
        private String title;
        private String content;
        private String  createdDate;
        //        마감시간은 없애고, 시간은 오전, 오후, 상관없음.
        private Status status;
        private String startTime;
        private Slot slot;
        private String location;
        private Long pay;
        private int volunteerTime;
        private MarketType marketType;
        private int maxNumOfPeople;
        private String category;
        private int commentCount;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @ApiModel(value = "거래 게시글 모음 ")
    public static class PostResponse {
        private Long postId;
        private String title;
        private String location;
        private Long pay;
        private int volunteerTime;
        private MarketType marketType;
        private String category;
        private Status status;
        private String createdDate;
        private int commentCount;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    @ApiModel(value = "id 별 게시글 + 댓글 ")
    public static class TransactionPostWithComment {
        private ProfileResponseDTO.UserInfo writerInfo;
        private Boolean isWriter;
        private Boolean isApplicant;
        private eum.backed.server.domain.community.apply.Status tradingStatus;
        private Boolean isScrap;
        private MarketType marketType;
        private Long postId;
        private String title;
        private String content;
        private Slot slot;
        private String startDate;
        private Long pay;
        private int volunteerTime;
        private String location;
        private int currentApplicant;
        private int maxNumOfPeople;
        private String category;
        private Status status;
        private String createdDate;
        private int commentCount;
        private List<CommentResponseDTO.CommentResponse> commentResponses;
    }
    @Builder
    @Getter
    @Setter
    private static class UserCurrentStatus{
        private Boolean isWriter;
        private Boolean isApplicant;
        private Boolean isScrap;
    }
    public static PostResponseDTO.MarketPostResponse singleMarketPost(MarketPost marketPost, int count){
        String createdTime = KoreaLocalDateTime.localDateTimeToKoreaZoned(marketPost.getCreateDate());
        String startTime = KoreaLocalDateTime.dateToKoreaZone(marketPost.getStartDate());
        return MarketPostResponse.builder()
                .postId(marketPost.getMarketPostId())
                .title(marketPost.getTitle())
                .createdDate(createdTime)
                .startTime(startTime)
                .slot(marketPost.getSlot())
                .content(marketPost.getContent())
                .pay(marketPost.getPay())
                .volunteerTime(marketPost.getVolunteerTime())
                .marketType(marketPost.getMarketType())
                .location(marketPost.getLocation())
                .category(marketPost.getMarketCategory().getContents())
                .status(marketPost.getStatus())
                .commentCount(count)
                .maxNumOfPeople(marketPost.getMaxNumOfPeople())
                .build();
    }
    public static PostResponseDTO.PostResponse newPostResponse(MarketPost marketPost){
        String createdTime = KoreaLocalDateTime.localDateTimeToKoreaZoned(marketPost.getCreateDate());
        return PostResponse.builder()
                .postId(marketPost.getMarketPostId())
                .title(marketPost.getTitle())
                .createdDate(createdTime)
                .pay(marketPost.getPay())
                .volunteerTime(marketPost.getVolunteerTime())
                .marketType(marketPost.getMarketType())
                .location(marketPost.getLocation())
                .category(marketPost.getMarketCategory().getContents())
                .status(marketPost.getStatus())
                .commentCount(marketPost.getMarketComments().size())
                .build();
    }
    public TransactionPostWithComment newTransactionPostWithComment(Users user, MarketPost marketPost, List<CommentResponseDTO.CommentResponse> commentResponses, Boolean isApply, Boolean isScrap, eum.backed.server.domain.community.apply.Status tradingStatus){
        String createdTime = KoreaLocalDateTime.localDateTimeToKoreaZoned(marketPost.getCreateDate());
        String startTime = KoreaLocalDateTime.dateToKoreaZone(marketPost.getStartDate());
        return TransactionPostWithComment.builder()
                .writerInfo(ProfileResponseDTO.toUserInfo(marketPost.getUser()))
                .isScrap(isScrap)
                .isWriter(user == marketPost.getUser())
                .isApplicant(isApply)
                .tradingStatus(tradingStatus)
                .postId(marketPost.getMarketPostId())
                .title(marketPost.getTitle())
                .content(marketPost.getContent())
                .startDate(startTime)
                .createdDate(createdTime)
                .pay(marketPost.getPay())
                .location(marketPost.getLocation())
                .volunteerTime(marketPost.getVolunteerTime())
                .marketType(marketPost.getMarketType())
                .currentApplicant(marketPost.getApplies().size())
                .maxNumOfPeople(marketPost.getMaxNumOfPeople())
                .category(marketPost.getMarketCategory().getContents())
                .status(marketPost.getStatus())
                .slot(marketPost.getSlot())
                .commentResponses(commentResponses)
                .commentCount(commentResponses.size())
                .build();
    }
}
