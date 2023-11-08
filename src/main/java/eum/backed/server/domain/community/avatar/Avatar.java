package eum.backed.server.domain.community.avatar;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.profile.Profile;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Avatar extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long avatarId;

    @Column
    private String name;
    private String avatarPhotoUrl;
//    @Column
//    @Enumerated(EnumType.STRING)
//    private AvatarName avatarName;

    @OneToOne(mappedBy = "avatar")
    private Profile profile;

    @OneToOne
    @JoinColumn(name = "standard_id")
    private Standard standard;

}
