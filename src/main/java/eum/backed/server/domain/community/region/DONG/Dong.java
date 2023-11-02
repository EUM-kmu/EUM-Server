package eum.backed.server.domain.community.region.DONG;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.region.GU.Gu;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
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
public class Dong extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long DongId;

    @Column
    private String dong;

    @ManyToOne
    @JoinColumn(name="gu_id")
    private Gu gu;


    @OneToMany(mappedBy = "dong")
    private List<TransactionPost> transactionPosts = new ArrayList<>();

    @OneToMany(mappedBy = "dong")
    private List<OpinionPost> opinionPosts = new ArrayList<>();

    @OneToMany(mappedBy = "dong")
    private List<VotePost> votePosts = new ArrayList<>();

}
