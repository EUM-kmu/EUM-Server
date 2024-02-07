package eum.backed.server.controller.community.DTO.response;

import eum.backed.server.domain.auth.user.Users;
import eum.backed.server.domain.community.profile.Profile;
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
        private String nickName;
        private String address;
        private String profileImage; //네이버 클라우드 Url

    }

    public static ProfileResponse toProfileResponse(Profile profile){
        return ProfileResponse.builder()
                .nickName(profile.getNickname())
                .address("주소였던것")
                .profileImage(profile.getProfileImage())
                .build();
    }

    @Getter
    @Setter
    @Builder
    public static class UserInfo{
        private Long userId;
        private String nickName;
        private String profileImage;
        private String address;
    }
    public static UserInfo toUserInfo(Users user){
        return UserInfo.builder()
                .userId(user.getUserId())
                .nickName(user.getProfile().getNickname())
                .profileImage(user.getProfile().getProfileImage())
                .address("주소였던것").build();
    }


}
