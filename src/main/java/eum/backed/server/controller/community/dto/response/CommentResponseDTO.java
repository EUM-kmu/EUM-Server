package eum.backed.server.controller.community.dto.response;

import eum.backed.server.domain.community.user.Users;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
        private ProfileResponseDTO.UserInfo writerInfo;
        private String commentContent;
        private Boolean isPostWriter;
        private Boolean isCommentWriter;
        private String createdTime;
    }

    public static CommentResponse newCommentResponse(Long postId, Long commentId, Boolean isCommentWriter, Boolean isPostWriter, String content, LocalDateTime createdTime, Users user){
        LocalDateTime utcDateTime = LocalDateTime.parse(createdTime.toString(), DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime koreaZonedDateTime = utcDateTime.atZone(ZoneId.of("Asia/Seoul"));
        // 한국 시간대로 포맷팅
        String formattedDateTime = koreaZonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
        return CommentResponse.builder()
                .postId(postId)
                .commentId(commentId)
                .isCommentWriter(isCommentWriter)
                .isPostWriter(isPostWriter)
                .commentContent(content)
                .createdTime(formattedDateTime)
                .writerInfo(ProfileResponseDTO.toUserInfo(user)).build();
    }

}
