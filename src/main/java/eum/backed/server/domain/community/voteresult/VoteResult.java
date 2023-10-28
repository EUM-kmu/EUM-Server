package eum.backed.server.domain.community.voteresult;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.votepost.VotePost;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VoteResult extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteResult;

    @Column
    private boolean agree;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="vote_post_id")
    private VotePost votePost;
}
