package eum.backed.server.commumityapi.domain.scrap;

import eum.backed.server.commumityapi.domain.BaseTimeEntity;
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
public class Scrap extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
}
