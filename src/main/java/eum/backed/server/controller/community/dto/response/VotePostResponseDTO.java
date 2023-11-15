package eum.backed.server.controller.community.dto.response;

import eum.backed.server.domain.community.votepost.VotePost;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class VotePostResponseDTO {
    @Getter
    @Setter
    public static class VotePostResponses{
        private Long userId;
        private String postTitle;
        private Boolean isVoting;
        private int commentCount;
        private LocalDateTime createdTime;

        public VotePostResponses(VotePost votePost) {
            LocalDateTime now = LocalDateTime.now();
            Date currentDate = Date.from(Instant.from(now));

            this.userId = votePost.getUser().getUserId();
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
        private List<CommentResponseDTO> commentResponseDTOS;
        private LocalDateTime createdTime;
    }

    public static VotePostWithComment newVotePostWithComment(VotePost votePost, List<CommentResponseDTO.CommentResponse> commentResponses,Boolean isVoting,Boolean amIVote){
        return VotePostWithComment.builder()
                .writerId(votePost.getUser().getUserId())
                .writerNickName(votePost.getUser().getProfile().getNickname())
                .writerAddress(votePost.getUser().getProfile().getAddress())
                .agreeCounts(votePost.getAgreeCount())
                .disagreeCount(votePost.getDisagreeCount())
                .postTitle(votePost.getTitle())
                .postContent(votePost.getContent())
                .voteEndDate(votePost.getEndTime())
                .isVoting(isVoting)
                .amIVote(amIVote)
                .commentResponseDTOS(commentResponseDTOS)
                .commentCount(commentResponseDTOS.size())
                .createdTime(votePost.getCreateDate()).build();
    }
}
