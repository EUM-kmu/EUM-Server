package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.community.marketpost.Slot;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.Status;
import eum.backed.server.domain.community.user.Users;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostResponseDTO {
    private final Time time;

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
        private LocalDateTime createdDate;
        private int commentCount;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    @ApiModel(value = "id 별 게시글 + 댓글 ")
    public static class TransactionPostWithComment {
        private ProfileResponseDTO.UserInfo writerInfo;
        private Boolean isWriter;
        private MarketType marketType;
        private Long postId;
        private String title;
        private String content;
        private Slot slot;
        private Date startDate;
        private Long pay;
        private int volunteerTime;
        private String location;
        private int currentApplicant;
        private int maxNumOfPeople;
        private String category;
        private Status status;
        private LocalDateTime createdDate;
        private int commentCount;
        private List<CommentResponseDTO.CommentResponse> commentResponses;
    }
    public static PostResponseDTO.PostResponse newPostResponse(MarketPost marketPost){
        return PostResponse.builder()
                .postId(marketPost.getMarketPostId())
                .title(marketPost.getTitle())
                .createdDate(marketPost.getCreateDate())
                .pay(marketPost.getPay())
                .volunteerTime(marketPost.getVolunteerTime())
                .marketType(marketPost.getMarketType())
                .location(marketPost.getLocation())
                .category(marketPost.getMarketCategory().getContents())
                .status(marketPost.getStatus())
                .commentCount(marketPost.getMarketComments().size())
                .build();
    }
    public TransactionPostWithComment newTransactionPostWithComment(Users user, MarketPost marketPost, List<CommentResponseDTO.CommentResponse> commentResponses){
        return TransactionPostWithComment.builder()
                .writerInfo(ProfileResponseDTO.toUserInfo(marketPost.getUser()))
                .postId(marketPost.getMarketPostId())
                .isWriter(user == marketPost.getUser())
                .title(marketPost.getTitle())
                .content(marketPost.getContents())
                .startDate(marketPost.getStartDate())
                .createdDate(marketPost.getCreateDate())
                .pay(marketPost.getPay())
                .location(marketPost.getLocation())
                .volunteerTime(marketPost.getVolunteerTime())
                .marketType(marketPost.getMarketType())
                .currentApplicant(marketPost.getCurrentAcceptedPeople())
                .maxNumOfPeople(marketPost.getMaxNumOfPeople())
                .category(marketPost.getMarketCategory().getContents())
                .status(marketPost.getStatus())
                .slot(marketPost.getSlot())
                .commentResponses(commentResponses)
                .commentCount(commentResponses.size())
                .build();
    }
}
