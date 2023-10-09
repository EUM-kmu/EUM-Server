package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.comment.Comment;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
@Component
@RequiredArgsConstructor
public class CommentResponseDTO {
    private final Time time;
    @Builder
    @Getter
    @AllArgsConstructor
    @ApiModel(value = "전체 데이터 정렬")
    public static class CommentResponse {
        private Long postId;
        private Long commentId;
        private String nickname;
        private String userAddress;
        private String customCreatedTime;
        private String content;
        private boolean writer;
    }

    public CommentResponse newCommentResponse(Comment comment, boolean writer){
        Date date = Date.from(comment.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
        return CommentResponse.builder()
                .postId(comment.getPost().getPostId())
                .commentId(comment.getCommentId())
                .nickname(comment.getUser().getNickname())
                .userAddress(comment.getUser().getAddress())
                .customCreatedTime(time.calculateTime(date))
                .content(comment.getContent())
                .writer(writer).build();
    }
}
