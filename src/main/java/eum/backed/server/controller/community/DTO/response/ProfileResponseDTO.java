package eum.backed.server.controller.community.DTO.response;

import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.auth.user.Role;
import eum.backed.server.domain.auth.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component

public class ProfileResponseDTO {

    @Getter
    @Setter
    @Builder
    public static class profilee{
        String nickName;
        Role role;

    }
    @Getter
    @Setter
    @Builder
    public static class ProfileResponse {
        private String nickname;
        private String address;
        private String avatarPhotoURL; //네이버 클라우드 Url
        private String characterName; //청소년, 청년, 중년 , 노인

    }

    public static ProfileResponse toProfileResponse(Profile profile){
        return ProfileResponse.builder()
                .nickname(profile.getNickname())
                .address("주소였던것")
                .avatarPhotoURL(profile.getAvatar().getAvatarPhotoUrl())
                .characterName(profile.getAvatar().getAvatarName().toString())
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
                .address("주소였던것").build();
    }

}
