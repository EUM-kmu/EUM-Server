package eum.backed.server.domain.community.comment;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.user.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OpinionComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long opinionCommentId;

    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="opinion_post_id")
    private OpinionPost opinionPost;

    public static OpinionComment toEntity(String comment, Users user, OpinionPost opinionPost){
        return OpinionComment.builder()
                .comment(comment)
                .user(user)
                .opinionPost(opinionPost).build();
    }
}
