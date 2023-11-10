package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.comment.OpinionComment;
import eum.backed.server.domain.community.comment.TransactionComment;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
@Component
@RequiredArgsConstructor
public class CommentResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @ApiModel(value = "전체 데이터 정렬")
    public static class CommentResponse {
        private Long postId;
        private Long commentId;
        private String commentNickName;
        private String commentUserAddress;
        private String commentContent;
        private Boolean isPostWriter;
        private LocalDateTime createdTime;
    }

    public CommentResponse newCommentResponse(Long postId, Long commentId,String commentWriter,String commentWriterAddress,String content,LocalDateTime createdTime, boolean writer){
        return CommentResponse.builder()
                .postId(postId)
                .commentId(commentId)
                .commentNickName(commentWriter)
                .commentUserAddress(commentWriterAddress)
                .commentContent(content)
                .createdTime(createdTime)
                .isPostWriter(writer).build();
    }

}
