package eum.backed.server.domain.bank.branchbankaccount;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.bank.Owner;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BranchBankAccount extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchBankAccountId;

    @Column
    private String password;
    private String accountName;

    @Column
    @Enumerated(EnumType.STRING)
    private Owner owner;


}
