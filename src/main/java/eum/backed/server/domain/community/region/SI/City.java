package eum.backed.server.domain.community.region.SI;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.region.GU.Town;
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
public class City extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    @Column
    private String name;

    @OneToMany(mappedBy = "city", orphanRemoval = true)
    private List<Town> townList = new ArrayList<>();
}
