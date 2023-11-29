package eum.backed.server.controller.community.dto.response;

import eum.backed.server.domain.community.votepost.VotePost;
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
    @Getter
    @Setter
    public static class VotePostResponses{
        private Long postId;
        private String postTitle;
        private Boolean isVoting;
        private int commentCount;
        private LocalDateTime createdTime;

        public VotePostResponses(VotePost votePost) {
            LocalDateTime now = LocalDateTime.now();
            ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
            Date currentDate = Date.from(zonedDateTime.toInstant());
            this.postId = votePost.getVotePostId();
            this.postTitle = votePost.getTitle();
            this.isVoting = currentDate.before(votePost.getEndTime());
            this.commentCount = votePost.getVoteComments().size();
            this.createdTime = votePost.getCreateDate();
        }
    }
    @Getter
    @Setter
    @Builder
    public static class VotePostWithComment{
        private Long writerId;
        private String writerNickName;
        private String writerAddress;
        private int agreeCounts;
        private int disagreeCount;
        private String postTitle;
        private String postContent;
        private Date voteEndDate;
        private Boolean isVoting;
        private Boolean amIVote;
        private int commentCount;
        private List<CommentResponseDTO.CommentResponse> commentResponses;
        private LocalDateTime createdTime;
    }

    public static VotePostWithComment newVotePostWithComment(VotePost votePost, List<CommentResponseDTO.CommentResponse> commentResponses,Boolean amIVote){
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
        Date currentDate = Date.from(zonedDateTime.toInstant());
        return VotePostWithComment.builder()
                .writerId(votePost.getUser().getUserId())
                .writerNickName(votePost.getUser().getProfile().getNickname())
                .writerAddress(votePost.getUser().getProfile().getRegions().getName())
                .agreeCounts(votePost.getAgreeCount())
                .disagreeCount(votePost.getDisagreeCount())
                .postTitle(votePost.getTitle())
                .postContent(votePost.getContent())
                .voteEndDate(votePost.getEndTime())
                .isVoting(currentDate.before(votePost.getEndTime()))
                .amIVote(amIVote)
                .commentResponses(commentResponses)
                .commentCount(commentResponses.size())
                .createdTime(votePost.getCreateDate()).build();
    }
}
