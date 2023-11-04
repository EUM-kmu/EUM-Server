package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.comment.TransactionComment;
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
        private String commentNickName;
        private String commentUserAddress;
        private String commentCustomCreatedTime;
        private String commentContent;
        private Boolean isPostWriter;
        private Boolean isCommentWriter;
    }

    public CommentResponse newCommentResponse(TransactionComment transactionComment, boolean writer){
        Date date = Date.from(transactionComment.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
        return CommentResponse.builder()
                .postId(transactionComment.getTransactionPost().getTransactionPostId())
                .commentId(transactionComment.getTransactionCommentId())
                .commentNickName(transactionComment.getUser().getProfile().getNickname())
                .commentUserAddress(transactionComment.getUser().getProfile().getDong().getDong())
                .commentCustomCreatedTime(time.calculateTime(date))
                .commentContent(transactionComment.getContent())
                .isPostWriter(writer).build();
    }
}
