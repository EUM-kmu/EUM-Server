package eum.backed.server.domain.community.mylevel;

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
public class AvatarLevel extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long avatarLevelId;

    @Column
    private int standard;
    private String levelName;
    private String avatarLevelPhotoUrl;

//    @Column
//    @Enumerated(EnumType.STRING)
//    private AvatarName avatarName;

    @OneToOne(mappedBy = "avatarLevel")
    private Profile profile;

}
