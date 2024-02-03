package eum.backed.server.domain.community.comment;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.auth.user.Users;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MarketComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long marketCommentId;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="transaction_post_id")
    private MarketPost marketPost;

    public void updateContent(String content) {
        this.content = content;
    }

    public static MarketComment toEntity(String content, Users user, MarketPost marketPost){
        return MarketComment.builder()
                .content(content)
                .user(user)
                .marketPost(marketPost).build();
    }
}
