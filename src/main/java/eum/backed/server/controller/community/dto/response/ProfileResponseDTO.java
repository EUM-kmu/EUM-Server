package eum.backed.server.controller.community.dto.response;

import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileResponseDTO {
    private Long userId;
    private Long profileId;
    private String name;
    private String introduction;
    private String nickname;
    private String address;
    private int totalSunrisePay;
    private String avatarPhotoURL;
    private Long balance;

    public static ProfileResponseDTO toNewProfileResponseDTO(Users user, Profile profile){
        String si = profile.getTownship().getTown().getCity().getName();
        String gu = profile.getTownship().getTown().getName();
        String dong = profile.getTownship().getName();
        String fullAddress = si + " " + gu + " " + dong;


        return ProfileResponseDTO.builder()
                .userId(user.getUserId())
                .profileId(profile.getProfileId())
                .nickname(profile.getNickname())
                .introduction(profile.getIntroduction())
                .address(fullAddress)
                .totalSunrisePay(profile.getTotalSunrisePay())
                .avatarPhotoURL(profile.getAvatar().getAvatarPhotoUrl())
                .balance(user.getUserBankAccount().getBalance())
                .build();
    }

}
