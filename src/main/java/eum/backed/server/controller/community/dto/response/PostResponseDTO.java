package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.transactionpost.Slot;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.transactionpost.Status;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
    @ApiModel(value = "전체 거래 게시글 데이터 ")
    public static class PostResponse {
        private Long postId;
        private String title;
        private String location;
        private int pay;
        private int volunteerTime;
        private boolean providingHelp;
        private String category;
        private Status status;
        private LocalDateTime createdDate;
        private int commentCount;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    @ApiModel(value = "전체 거래 게시글 데이터 ")
    public static class TransactionPostWithComment {
        private Long postId;
        private String title;
        private String content;
        private Date startDate;
        private int pay;
        private String location;
        private int volunteerTime;
        private boolean providingHelp;
        private int currentApplicant;
        private int maxNumOfPeople;
        private String category;
        private Status status;
        private Slot slot;
        private int commentCount;
        private LocalDateTime createdDate;
        private List<CommentResponseDTO.CommentResponse> commentResponses;
    }
    public PostResponseDTO.PostResponse newPostResponse(TransactionPost transactionPost){
        return PostResponse.builder()
                .postId(transactionPost.getTransactionPostId())
                .title(transactionPost.getTitle())
                .createdDate(transactionPost.getCreateDate())
                .pay(transactionPost.getPay())
                .volunteerTime(transactionPost.getVolunteerTime())
                .providingHelp(transactionPost.getProvidingHelp())
                .category(transactionPost.getTransactionCategory().getContents())
                .status(transactionPost.getStatus())
                .commentCount(transactionPost.getTransactionComments().size())
                .build();
    }
    public TransactionPostWithComment newTransactionPostWithComment(TransactionPost transactionPost, List<CommentResponseDTO.CommentResponse> commentResponses){
        return TransactionPostWithComment.builder()
                .postId(transactionPost.getTransactionPostId())
                .title(transactionPost.getTitle())
                .content(transactionPost.getContents())
                .startDate(transactionPost.getStartDate())
                .createdDate(transactionPost.getCreateDate())
                .pay(transactionPost.getPay())
                .location(transactionPost.getLocation())
                .volunteerTime(transactionPost.getVolunteerTime())
                .providingHelp(transactionPost.getProvidingHelp())
                .currentApplicant(transactionPost.getCurrentAcceptedPeople())
                .maxNumOfPeople(transactionPost.getMaxNumOfPeople())
                .category(transactionPost.getTransactionCategory().getContents())
                .status(transactionPost.getStatus())
                .slot(transactionPost.getSlot())
                .commentResponses(commentResponses)
                .commentCount(commentResponses.size())
                .build();
    }
}
