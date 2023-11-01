package eum.backed.server.controller.community.dto.response;

import eum.backed.server.common.DTO.Time;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
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
    public ApplyListResponse newApplyListResponse(TransactionPost transactionPost, Users applicant, Profile profile, Apply apply){
        Date date = Date.from(apply.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
        return ApplyListResponse.builder()
                .applyId(apply.getApplyId())
                .applicantId(applicant.getUserId())
                .applicantNickName(profile.getNickname())
                .applicantAddress(profile.getDong().getDong())
                .customCreatedTime(time.calculateTime(date))
                .introduction(apply.getContent())
                .postId(transactionPost.getTransactionPostId())
                .isAccepted(apply.getIsAccepted()).build();
    }
}
