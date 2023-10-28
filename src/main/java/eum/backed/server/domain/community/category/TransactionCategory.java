package eum.backed.server.domain.community.category;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column
    private String contents;

    @OneToMany(mappedBy = "transactionCategory", orphanRemoval = true)
    private List<TransactionPost> transactionPosts = new ArrayList<>();
}
