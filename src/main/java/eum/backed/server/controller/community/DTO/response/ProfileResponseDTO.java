package eum.backed.server.controller.community.DTO.response;

import eum.backed.server.domain.community.avatar.AvatarName;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.auth.user.Users;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.stereotype.Component;

@Component

public class ProfileResponseDTO {

    @Getter
    @Setter
    @Builder
    public static class ProfileResponse {
        private String nickName;
        private String address;
        private String avatarPhotoURL; //네이버 클라우드 Url

    }

    public static ProfileResponse toProfileResponse(Profile profile){
        return ProfileResponse.builder()
                .nickName(profile.getNickname())
                .address("주소였던것")
                .avatarPhotoURL(profile.getAvatar().getAvatarPhotoUrl())
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

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor

    public static class AvatarInfo{
        Long avatarId;
        String avatarPhotoUrl;
        String simpleAvatarPhotoUrl;
        AvatarName avatarName;
    }


}
