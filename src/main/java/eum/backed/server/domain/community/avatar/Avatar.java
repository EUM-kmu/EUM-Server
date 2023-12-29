package eum.backed.server.domain.community.avatar;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.profile.Profile;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long avatarId;

    @Column
    private String avatarPhotoUrl;
    private String simpleAvatarPhotoUrl;

    @Column
    @Enumerated(EnumType.STRING)
    private AvatarLevelName avatarLevelName;
    @Column
    @Enumerated(EnumType.STRING)
    private AvatarName avatarName;

    @OneToMany(mappedBy = "avatar")
    private List<Profile> profiles = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "standard_id")
    private Standard standard;

}
