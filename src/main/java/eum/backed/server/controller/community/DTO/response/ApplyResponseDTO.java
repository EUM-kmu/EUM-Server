package eum.backed.server.controller.community.DTO.response;

import eum.backed.server.common.KoreaLocalDateTime;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.apply.Status;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.auth.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplyResponseDTO {
    @Getter
    @Builder
    public static class ApplyListResponse {
        private Long applyId;
        private ProfileResponseDTO.UserInfo applicantInfo;
        private String createdTime;
        private Status status;
        private String introduction;
        private Long postId;
        private Boolean isAccepted;
    }
    public ApplyListResponse newApplyListResponse(MarketPost marketPost, Users applicant, Profile profile, Apply apply){
        ProfileResponseDTO.UserInfo applicantInfo = ProfileResponseDTO.UserInfo.builder().userId(applicant.getUserId()).profileImage(profile.getProfileImage()).nickName(profile.getNickname()).address(profile.getAddress()).build();
        // 한국 시간대로 포맷팅
        String formattedCreateTime = KoreaLocalDateTime.localDateTimeToKoreaZoned(apply.getCreateDate());
        return ApplyListResponse.builder()
                .applyId(apply.getApplyId())
                .applicantInfo(applicantInfo)
                .status(apply.getStatus())
                .createdTime(formattedCreateTime)
                .introduction(apply.getContent())
                .postId(marketPost.getMarketPostId())
                .isAccepted(apply.getIsAccepted()).build();
    }
}
