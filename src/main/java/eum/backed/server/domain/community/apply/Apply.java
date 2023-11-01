package eum.backed.server.domain.community.apply;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.controller.community.dto.request.ApplyRequestDTO;
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
public class Apply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applyId;

    @Column
    private Boolean isAccepted;
    private String content;

    @ManyToOne
    @JoinColumn(name="applicant_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="transaction_post_id")
    private TransactionPost transactionPost;

    public void updateAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public static Apply toEntity(String introduction, Users user, TransactionPost transactionPost){
        return Apply.builder()
                .content(introduction)
                .user(user)
                .isAccepted(Boolean.FALSE)
                .transactionPost(transactionPost).build();
    }
}
