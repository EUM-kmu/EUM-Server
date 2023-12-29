package eum.backed.server.domain.bank.branchbankaccount;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.bank.branchbankaccountlog.BranchBankAccountLog;
import eum.backed.server.enums.Owner;
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

    @OneToMany(mappedBy = "branchBankAccount")
    private List<BranchBankAccountLog> branchBankAccountLogs = new ArrayList<>();

}
