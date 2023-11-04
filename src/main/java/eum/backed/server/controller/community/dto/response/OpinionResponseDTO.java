package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.time.ZoneId;
import java.util.Date;
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
    }
    @Getter
    @Setter
    @Builder
    public static class OpinionPostWithComment{
        private Long userId;
        private String writerNickName;
        private String title;
        private String postContent;
        private String postCustomCreatedTime;
        private String userAddress;
        private int likeCount;
        private int commentCount;
        private List<CommentResponseDTO.CommentResponse> commentResponses;
    }
    public AllOpinionPostsResponses newOpinionPostsResponse(OpinionPost opinionPost){
        Date date = Date.from(opinionPost.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
        return AllOpinionPostsResponses.builder()
                .userId(opinionPost.getUser().getUserId())
                .nickName(opinionPost.getUser().getProfile().getNickname())
                .userAddress(opinionPost.getUser().getProfile().getDong().getDong())
                .title(opinionPost.getTitle())
                .content(opinionPost.getContent())
                .customCreatedTime(time.calculateTime(date))
                .build();
    }
//    public AllOpinionPostsResponses newOpinionPostWithComment(OpinionPost opinionPost, List<CommentResponseDTO.CommentResponse> commentResponseDTO){
//        Date date = Date.from(opinionPost.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
//
//    }
}
