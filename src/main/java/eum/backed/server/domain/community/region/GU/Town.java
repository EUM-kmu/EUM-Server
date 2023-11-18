package eum.backed.server.domain.community.region.GU;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.region.DONG.Township;
import eum.backed.server.domain.community.region.SI.City;
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
public class Town extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long townId;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name="city_id")
    private City city;

    @OneToMany(mappedBy = "town", orphanRemoval = true)
    private List<Township> townshipList = new ArrayList<>();



}
