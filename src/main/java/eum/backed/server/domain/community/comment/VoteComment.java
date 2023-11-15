package eum.backed.server.domain.community.comment;

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
public class VoteComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteCommentId;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "vote_post_id")
    private VotePost votePost;

    public void updateContent(String content) {
        this.content = content;
    }

    public static VoteComment toEntity(String content, Users user, VotePost votePost){
        return VoteComment.builder()
                .content(content)
                .user(user)
                .votePost(votePost).build();
    }
}
