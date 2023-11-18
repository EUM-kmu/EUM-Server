package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpinionResponseDTO {
    private final Time time;
    @Getter
    @Setter
    @Builder
    public static class AllOpinionPostsResponses {
        private Long userId;
        private String nickName;
        private String title;
        private String content;
        private String customCreatedTime;
        private String userAddress;
        private int likeCount;
        private int commentCount;
        private LocalDateTime createdTime;
    }
    @Getter
    @Setter
    @Builder
    public static class OpinionPostWithComment{
        private Long userId;
        private String writerNickName;
        private String title;
        private String postContent;
        private String userAddress;
        private int likeCount;
        private int commentCount;
        private LocalDateTime createdTime;
        private List<CommentResponseDTO.CommentResponse> commentResponses;
    }
    public AllOpinionPostsResponses newOpinionPostsResponse(OpinionPost opinionPost){
        return AllOpinionPostsResponses.builder()
                .userId(opinionPost.getUser().getUserId())
                .nickName(opinionPost.getUser().getProfile().getNickname())
                .userAddress(opinionPost.getUser().getProfile().getTownship().getName())
                .title(opinionPost.getTitle())
                .likeCount(opinionPost.getLikeCount())
                .content(opinionPost.getContent())
                .createdTime(opinionPost.getCreateDate())
                .build();
    }
    public OpinionPostWithComment newOpinionPostWithComment(OpinionPost opinionPost, List<CommentResponseDTO.CommentResponse> commentResponseDTO){
        return OpinionPostWithComment.builder()
                .userId(opinionPost.getUser().getUserId())
                .writerNickName(opinionPost.getUser().getProfile().getNickname())
                .userAddress(opinionPost.getUser().getProfile().getTownship().getName())
                .title(opinionPost.getTitle())
                .postContent(opinionPost.getContent())
                .createdTime(opinionPost.getCreateDate())
                .likeCount(opinionPost.getLikeCount())
                .commentCount(commentResponseDTO.size())
                .commentResponses(commentResponseDTO).build();
    }
}
