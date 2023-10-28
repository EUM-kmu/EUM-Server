package eum.backed.server.domain.community.mylevel;

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
public class MyLevel extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myLevelId;

    @Column
    private int standard;
    private int level;

    @OneToMany(mappedBy = "myLevel", orphanRemoval = true)
    private List<Profile> profiles  = new ArrayList<>();

}
