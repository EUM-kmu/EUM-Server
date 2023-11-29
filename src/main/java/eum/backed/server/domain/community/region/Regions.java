package eum.backed.server.domain.community.region;

import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.profile.Profile;
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
public class Regions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionId;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private RegionType regionType;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private eum.backed.server.domain.community.region.Regions parent;

    @OneToMany(mappedBy = "parent")
    private List<Regions> children = new ArrayList<>();

    @OneToMany(mappedBy = "regions")
    private List<Profile> profiles = new ArrayList<>();

    @OneToMany(mappedBy = "regions")
    private List<MarketPost> marketPosts = new ArrayList<>();

    @OneToMany(mappedBy = "regions")
    private List<OpinionPost> opinionPosts = new ArrayList<>();

    @OneToMany(mappedBy = "regions")
    private List<VotePost> votePosts = new ArrayList<>();

}
