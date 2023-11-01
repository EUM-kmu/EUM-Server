package eum.backed.server.controller.community.dto.response;

import com.google.auto.value.AutoValue;
import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.time.ZoneId;
import java.util.Date;
@Component
@RequiredArgsConstructor
public class OpinionResponseDTO {
    private final Time time;
    @Getter
    @Setter
    @Builder
    public static class OpinionPostsResponse{
        private Long userId;
        private String nickName;
        private String title;
        private String content;
        private String customCreatedTime;
        private String userAddress;
    }
    public OpinionPostsResponse newOpinionPostsResponse(OpinionPost opinionPost){
        Date date = Date.from(opinionPost.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
        return OpinionPostsResponse.builder()
                .userId(opinionPost.getUser().getUserId())
                .nickName(opinionPost.getUser().getProfile().getNickname())
                .userAddress(opinionPost.getUser().getProfile().getDong().getDong())
                .title(opinionPost.getTitle())
                .content(opinionPost.getContent())
                .customCreatedTime(time.calculateTime(date))
                .build();


    }
}
