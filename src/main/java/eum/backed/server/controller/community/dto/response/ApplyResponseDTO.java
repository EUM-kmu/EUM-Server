package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.apply.Status;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.user.Users;
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
public class ApplyResponseDTO {
    private final Time time;
    @Getter
    @Builder
    public static class ApplyListResponse {
        private Long applyId;
        private String createdTime;
        private Status status;
        private Long applicantId;
        private String avatarPhotoUrl;
        private String applicantNickName;
        private String applicantAddress;
        private String introduction;
        private Long postId;
        private Boolean isAccepted;
    }
    public ApplyListResponse newApplyListResponse(MarketPost marketPost, Users applicant, Profile profile, Apply apply){

        // UTC 시간을 한국 시간대로 변환
        LocalDateTime createUTC = LocalDateTime.parse(apply.getCreateDate().toString(), DateTimeFormatter.ISO_DATE_TIME);
//        ZonedDateTime koreaZonedDateTime = utcDateTime.atZone(ZoneId.of("Asia/Seoul"));

        // 한국 시간대로 포맷팅
        ZonedDateTime koreaZonedCreateime = createUTC.atZone(ZoneId.of("Asia/Seoul"));
        String formattedCreateTime = koreaZonedCreateime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        return ApplyListResponse.builder()
                .applyId(apply.getApplyId())
                .applicantId(applicant.getUserId())
                .avatarPhotoUrl(profile.getAvatar().getSimpleAvatarPhotoUrl())
                .status(apply.getStatus())
                .applicantNickName(profile.getNickname())
                .applicantAddress(profile.getRegions().getName())
                .createdTime(formattedCreateTime)
                .introduction(apply.getContent())
                .postId(marketPost.getMarketPostId())
                .isAccepted(apply.getIsAccepted()).build();
    }
}
