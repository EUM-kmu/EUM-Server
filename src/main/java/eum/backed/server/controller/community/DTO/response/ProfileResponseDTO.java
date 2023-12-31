package eum.backed.server.controller.community.DTO.response;

import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.region.RegionType;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component

public class ProfileResponseDTO {
    @Getter
    @Setter
    @Builder
    public static class ProfileResponse {
        private Long userId;
        private Long profileId;
        private String introduction;
        private String nickname;
        private String address;
        private String avatarPhotoURL; //네이버 클라우드 Url
        private String characterName; //청소년, 청년, 중년 , 노인
        private String levelName; // 먹구름, 아기 햇님, 햇님
        private int totalSunrisePay;
        private int nextStandard;
        private Role role;

    }

    public static ProfileResponse toProfileResponse(Users user, Profile profile, int nextStandard){
        if(profile.getRegions().getRegionType()!= RegionType.DONG) throw new IllegalArgumentException("주소는 행정동만 들어갈수있습니다");
        String si = profile.getRegions().getParent().getParent().getName();
        String gu = profile.getRegions().getParent().getName();
        String dong = profile.getRegions().getName();
        String fullAddress = si + " " + gu + " " + dong;

        return ProfileResponse.builder()
                .userId(user.getUserId())
                .profileId(profile.getProfileId())
                .nickname(profile.getNickname())
                .introduction(profile.getIntroduction())
                .address(fullAddress)
                .role(user.getRole())
                .totalSunrisePay(profile.getTotalSunrisePay())
                .avatarPhotoURL(profile.getAvatar().getAvatarPhotoUrl())
                .characterName(profile.getAvatar().getAvatarName().toString())
                .nextStandard(nextStandard)
                .levelName(profile.getAvatar().getStandard().getName())
                .build();
    }

    @Getter
    @Setter
    @Builder
    public static class UserInfo{
        private Long userId;
        private String nickName;
        private String avatarPhotoUrl;
        private String address;
    }
    public static UserInfo toUserInfo(Users user){
        return UserInfo.builder()
                .userId(user.getUserId())
                .nickName(user.getProfile().getNickname())
                .avatarPhotoUrl(user.getProfile().getAvatar().getSimpleAvatarPhotoUrl())
                .address(user.getProfile().getRegions().getName()).build();
    }

}
