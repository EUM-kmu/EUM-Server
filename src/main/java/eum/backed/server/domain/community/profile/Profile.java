package eum.backed.server.domain.community.profile;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.controller.community.DTO.request.ProfileRequestDTO;
import eum.backed.server.domain.community.avatar.Avatar;
import eum.backed.server.domain.community.user.Users;
import lombok.*;

import javax.persistence.*;

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



    @ManyToOne
    @JoinColumn(name="avatar_id")
    private Avatar avatar;

    @OneToOne
    @JoinColumn(name="user_id")
    private Users user;


    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }

    public void updateAddress(String address) {
        this.address = address;
    }


    public void upDateAvatar(Avatar avatar) {
        this.avatar = avatar;
    }


    public static Profile toEntity(ProfileRequestDTO.CreateProfile createProfile, Avatar avatar, Users user){
        return Profile.builder()
                .nickname(createProfile.getNickname())
                .avatar(avatar)
                .user(user)
                .build();
    }

}
