package eum.backed.server.domain.community.profile;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.controller.community.DTO.request.ProfileRequestDTO;
import eum.backed.server.domain.auth.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Profile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column
    private String name;
    private String nickname;
    private String address;
    private String profileImage;




    @OneToOne
    @JoinColumn(name="user_id")
    private Users user;


    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }

    public void updateAddress(String address) {
        this.address = address;
    }




    public static Profile toEntity(ProfileRequestDTO.CreateProfile createProfile, Users user){
        return Profile.builder()
                .nickname(createProfile.getNickname())
                .user(user)
                .build();
    }

}
