package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.votepost.VotePost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
@Component
public class VotePostResponseDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class MyActivity {
        private boolean isWriter;
        private boolean doVote;
    }
    @Getter
    @Setter
    @Builder
    public static class SavedVotePost{
        private ProfileResponseDTO.UserInfo writerInfo;
        private int agreeCounts;
        private int disagreeCount;
        private String createdTime;
        private String title;
        private String content;
        private String voteEndDate;
        private int commentCount;
    }
    public static SavedVotePost toSaveResponse(VotePost votePost, Users getUser,int agreeCount, int disagreeCount,int commentCount){
        ProfileResponseDTO.UserInfo writerInfo = ProfileResponseDTO.toUserInfo(getUser);
        String createdTime = Time.localDateTimeToKoreaZoned(votePost.getCreateDate());
        String voteEndTime = Time.dateToKoreaZone(votePost.getEndTime());
        return SavedVotePost.builder()
                .writerInfo(writerInfo)
                .agreeCounts(agreeCount)
                .disagreeCount(disagreeCount)
                .title(votePost.getTitle())
                .content(votePost.getContent())
                .createdTime(createdTime)
                .voteEndDate(voteEndTime)
                .commentCount(commentCount).build();
    }
    @Getter
    @Setter
    public static class VotePostResponses{
        private String userAddress;
        private Long postId;
        private String title;
        private String voteEndTime;
        private int commentCount;
        private String createdTime;

        public VotePostResponses(VotePost votePost) {
            String createdTime = Time.localDateTimeToKoreaZoned(votePost.getCreateDate());
            String voteEndTime = Time.dateToKoreaZone(votePost.getEndTime());
            this.userAddress = votePost.getUser().getProfile().getRegions().getName();
            this.postId = votePost.getVotePostId();
            this.title = votePost.getTitle();
            this.voteEndTime = voteEndTime;
            this.commentCount = votePost.getVoteComments().size();
            this.createdTime = createdTime;
        }
    }
    @Getter
    @Setter
    @Builder
    public static class VotePostWithComment{
        private ProfileResponseDTO.UserInfo writerInfo;
        private int agreeCounts;
        private int disagreeCount;
        private String createdTime;
        private String title;
        private String content;
        private String voteEndDate;
        private MyActivity myActivity;
        private int commentCount;
        private List<CommentResponseDTO.CommentResponse> commentResponses;
    }

    public static VotePostWithComment newVotePostWithComment(VotePost votePost, List<CommentResponseDTO.CommentResponse> commentResponses, boolean doVote, Users user){
        String createdTime = Time.localDateTimeToKoreaZoned(votePost.getCreateDate());
        String voteEndTime = Time.dateToKoreaZone(votePost.getEndTime());
        ProfileResponseDTO.UserInfo writerInfo = ProfileResponseDTO.toUserInfo(user);
        MyActivity myActivity = MyActivity.builder().doVote(doVote).isWriter(votePost.getUser() == user).build();
        return VotePostWithComment.builder()
                .writerInfo(writerInfo)
                .agreeCounts(votePost.getAgreeCount())
                .disagreeCount(votePost.getDisagreeCount())
                .title(votePost.getTitle())
                .content(votePost.getContent())
                .voteEndDate(voteEndTime)
                .myActivity(myActivity)
                .commentResponses(commentResponses)
                .commentCount(commentResponses.size())
                .createdTime(createdTime).build();
    }
}
