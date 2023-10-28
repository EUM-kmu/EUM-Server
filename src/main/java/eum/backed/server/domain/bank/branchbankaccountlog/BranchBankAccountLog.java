package eum.backed.server.domain.bank.branchbankaccountlog;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.admin.admin.Admin;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccount;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BranchBankAccountLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long BranchBankAccountLogId;

    @ManyToOne
    @JoinColumn(name = "branch_bank_account_id")
    private BranchBankAccount branchBankAccount;

    @ManyToOne
    @JoinColumn(name = "admin")
    private Admin admin;

}
