package eum.backed.server.domain.community.likeopinionpost;

import eum.backed.server.domain.community.opinionpost.OpinionPost;
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
public class LikeOpinionPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeOpinionPostId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="opinion_post_id")
    private OpinionPost opinionPost;
}
