package eum.backed.server.domain.community.region.DONG;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.region.GU.Town;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.votepost.VotePost;
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
public class Township  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long townshipId;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name="town_id")
    private Town town;

    @OneToMany(mappedBy = "township")
    private List<Profile> profiles = new ArrayList<>();

    @OneToMany(mappedBy = "township")
    private List<MarketPost> marketPosts = new ArrayList<>();

    @OneToMany(mappedBy = "township")
    private List<OpinionPost> opinionPosts = new ArrayList<>();

    @OneToMany(mappedBy = "township")
    private List<VotePost> votePosts = new ArrayList<>();

}
