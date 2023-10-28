package eum.backed.server.domain.community.comment;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
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
    private TransactionPost transactionPost;

    public void updateContent(String content) {
        this.content = content;
    }
}
