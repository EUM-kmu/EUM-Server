package eum.backed.server.controller.community.dto.response;

import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.region.RegionType;
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
    public static class AllProfile{
        private Long userId;
        private Long profileId;
        private String introduction;
        private String nickname;
        private String address;
        private String avatarPhotoURL;
        private Long balance;
        private String characterName;
        private String levelName;
        private int totalSunrisePay;
        private int nextStandard;

    }

    public static AllProfile toNewProfileResponseDTO(Long balance,Users user, Profile profile,int nextStandard){
        if(profile.getRegions().getRegionType()!= RegionType.DONG) throw new IllegalArgumentException("행정동이 들어가야합니다");
        String si = profile.getRegions().getParent().getParent().getName();
        String gu = profile.getRegions().getParent().getName();
        String dong = profile.getRegions().getName();
        String fullAddress = si + " " + gu + " " + dong;

        return AllProfile.builder()
                .userId(user.getUserId())
                .profileId(profile.getProfileId())
                .nickname(profile.getNickname())
                .introduction(profile.getIntroduction())
                .address(fullAddress)
                .totalSunrisePay(profile.getTotalSunrisePay())
                .avatarPhotoURL(profile.getAvatar().getAvatarPhotoUrl())
                .balance(balance)
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
                .avatarPhotoUrl(user.getProfile().getAvatar().getAvatarPhotoUrl())
                .address(user.getProfile().getRegions().getName()).build();
    }

}
