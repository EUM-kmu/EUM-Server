package eum.backed.server.commumityapi.domain.comment;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.commumityapi.domain.post.Post;
import eum.backed.server.commumityapi.domain.user.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long commentId;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
}
