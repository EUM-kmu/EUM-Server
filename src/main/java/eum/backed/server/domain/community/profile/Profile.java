package eum.backed.server.domain.community.profile;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.controller.community.dto.request.ProfileRequestDTO;
import eum.backed.server.domain.community.avatar.Avatar;
import eum.backed.server.domain.community.region.DONG.Township;
import eum.backed.server.domain.community.user.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
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
    private String introduction;
    private String nickname;
    private String address;
    private int totalSunrisePay;


    @ManyToOne
    @JoinColumn(name = "township_id")
    private Township township;

    @ManyToOne
    @JoinColumn(name="avatar_id")
    private Avatar avatar;

    @OneToOne
    @JoinColumn(name="user_id")
    private Users user;

    public void updateInstroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updateTownship(Township township) {
        this.township = township;
    }

    public void upDateAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void addTotalSunrisePay(int totalSunrisePay) {
        this.totalSunrisePay += totalSunrisePay;
    }

    public static Profile t0Entity(ProfileRequestDTO.CreateProfile createProfile, Township townShip, Avatar avatar, Users user){
        return Profile.builder()
                .nickname(createProfile.getNickname())
                .township(townShip)
                .introduction(createProfile.getIntroduction())
                .totalSunrisePay(0)
                .avatar(avatar)
                .user(user)
                .build();
    }

}
