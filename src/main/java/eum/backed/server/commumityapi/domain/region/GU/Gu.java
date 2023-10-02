package eum.backed.server.commumityapi.domain.region.GU;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.commumityapi.domain.user.Users;
import eum.backed.server.commumityapi.domain.post.Post;
import eum.backed.server.commumityapi.domain.region.SI.Si;
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
public class Gu extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guId;

    @Column
    private String gu;

    @ManyToOne
    @JoinColumn(name="si_id")
    private Si si;

    @OneToMany(mappedBy = "gu", orphanRemoval = true)
    private List<Post>posts = new ArrayList<>();

    @OneToMany(mappedBy = "gu", orphanRemoval = true)
    private List<Users> usersList = new ArrayList<>();

}
