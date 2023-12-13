package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.user.Users;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpinionResponseDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class MyActivity {
        private boolean isWriter;
        private boolean doLike;
    }
    @Getter
    @Setter
    @Builder
    public static class AllOpinionPostsResponses {
        private Long postId;
        private String title;
        private String createdTime;
        private String userAddress;
        private int likeCount;
        private int commentCount;
    }
    @Getter
    @Setter
    @Builder
    public static class OpinionPostWithComment{
        private ProfileResponseDTO.UserInfo writerInfo;
        private Long postId;
        private String title;
        private String content;
        private String createdTime;
        private int likeCount;
        private int commentCount;
        private MyActivity myActivity;
        private List<CommentResponseDTO.CommentResponse> commentResponses;
    }

    @Getter
    @Setter
    @Builder
    public static class SavedOpinionResponse {
        private ProfileResponseDTO.UserInfo writerInfo;
        private String title;
        private String content;
        private String createdTime;
        private int likeCount;
        private int commentCount;
    }
    public static SavedOpinionResponse toCreateResponse(OpinionPost opinionPost, Users getUser){
        String createdTime = Time.localDateTimeToKoreaZoned(opinionPost.getCreateDate());
        ProfileResponseDTO.UserInfo writerInfo = ProfileResponseDTO.toUserInfo(getUser);
        return SavedOpinionResponse.builder()
                .writerInfo(writerInfo)
                .title(opinionPost.getTitle())
                .content(opinionPost.getContent())
                .createdTime(createdTime)
                .likeCount(0)
                .commentCount(0).build();
    }

    public AllOpinionPostsResponses newOpinionPostsResponse(OpinionPost opinionPost){
        String createdTime = Time.localDateTimeToKoreaZoned(opinionPost.getCreateDate());
        return AllOpinionPostsResponses.builder()
                .postId(opinionPost.getOpinionPostId())
                .title(opinionPost.getTitle())
                .createdTime(createdTime)
                .userAddress(opinionPost.getUser().getProfile().getRegions().getName())
                .likeCount(opinionPost.getLikeOpinionPosts().size())
                .commentCount(opinionPost.getOpinionComments().size())
                .build();
    }
    public OpinionPostWithComment newOpinionPostWithComment(OpinionPost opinionPost, List<CommentResponseDTO.CommentResponse> commentResponseDTO, Users user,boolean doLike){
        String createdTime = Time.localDateTimeToKoreaZoned(opinionPost.getCreateDate());
        ProfileResponseDTO.UserInfo writerInfo = ProfileResponseDTO.toUserInfo(opinionPost.getUser());
        MyActivity myActivity = MyActivity.builder().isWriter(opinionPost.getUser() == user).doLike(doLike).build();
        return OpinionPostWithComment.builder()
                .postId(opinionPost.getOpinionPostId())
                .writerInfo(writerInfo)
                .title(opinionPost.getTitle())
                .content(opinionPost.getContent())
                .createdTime(createdTime)
                .likeCount(opinionPost.getLikeOpinionPosts().size())
                .commentCount(commentResponseDTO.size())
                .myActivity(myActivity)
                .commentResponses(commentResponseDTO).build();
    }
}
