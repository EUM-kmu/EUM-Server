package eum.backed.server.domain.community.profile;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.controller.community.dto.request.ProfileRequestDTO;
import eum.backed.server.domain.community.mylevel.MyLevel;
import eum.backed.server.domain.community.region.DONG.Dong;
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

    @Column
    @Enumerated(EnumType.STRING)
    private Avatar avatar;

    @ManyToOne
    @JoinColumn(name = "dong_id")
    private Dong dong;

    @ManyToOne
    @JoinColumn(name="my_leve_id")
    private MyLevel myLevel;

    @OneToOne
    @JoinColumn(name="ueer_id")
    private Users user;

    public static Profile toEntiry(ProfileRequestDTO.CreateProfile createProfile,Dong dong ,MyLevel myLevel,Users user){
        return Profile.builder()
                .nickname(createProfile.getNickname())
                .introduction(createProfile.getIntroduction())
                .totalSunrisePay(0)
                .myLevel(myLevel)
                .user(user)
                .avatar(createProfile.getAvatar())
                .build();
    }

}
