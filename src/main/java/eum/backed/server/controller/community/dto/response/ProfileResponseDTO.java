package eum.backed.server.controller.community.dto.response;

import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;
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
        private int totalSunrisePay;
        private String avatarPhotoURL;
        private Long balance;

    }

    public static AllProfile toNewProfileResponseDTO(Long balance,Users user, Profile profile){
        String si = profile.getTownship().getTown().getCity().getName();
        String gu = profile.getTownship().getTown().getName();
        String dong = profile.getTownship().getName();
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
                .address(user.getProfile().getTownship().getName()).build();
    }

}
