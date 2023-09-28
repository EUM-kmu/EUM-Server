package eum.backed.server.commumityapi.domain.region.SI;

import eum.backed.server.commumityapi.domain.BaseTimeEntity;
import eum.backed.server.commumityapi.domain.region.GU.Gu;
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
public class Si extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long siId;

    @Column
    private String si;

    @OneToMany(mappedBy = "si", orphanRemoval = true)
    private List<Gu> guList = new ArrayList<>();
}
