package eum.backed.server.domain.community.comment;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.user.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long transactionCommentId;

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

    public static TransactionComment toEntity(String content, Users user, MarketPost marketPost){
        return TransactionComment.builder()
                .content(content)
                .user(user)
                .marketPost(marketPost).build();
    }
}
