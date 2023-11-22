package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
@Component
@RequiredArgsConstructor
public class ApplyResponseDTO {
    private final Time time;
    @Getter
    @Builder
    public static class ApplyListResponse {
        private Long applyId;
        private Long applicantId;
        private String applicantNickName;
        private String customCreatedTime;
        private String applicantAddress;
        private String introduction;
        private Long postId;
        private Boolean isAccepted;
    }
    public ApplyListResponse newApplyListResponse(MarketPost marketPost, Users applicant, Profile profile, Apply apply){
        Date date = Date.from(apply.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
        return ApplyListResponse.builder()
                .applyId(apply.getApplyId())
                .applicantId(applicant.getUserId())
                .applicantNickName(profile.getNickname())
                .applicantAddress(profile.getTownship().getName())
                .customCreatedTime(time.calculateTime(date))
                .introduction(apply.getContent())
                .postId(marketPost.getMarketPostId())
                .isAccepted(apply.getIsAccepted()).build();
    }
}
