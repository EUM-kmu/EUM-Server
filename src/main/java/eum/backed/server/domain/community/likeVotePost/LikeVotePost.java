package eum.backed.server.domain.community.likeVotePost;

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
public class LikeVotePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeVotePostId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="vote_post_id")
    private VotePost votePost;

}
