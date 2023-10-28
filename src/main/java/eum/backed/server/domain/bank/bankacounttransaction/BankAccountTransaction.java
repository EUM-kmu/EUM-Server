package eum.backed.server.domain.bank.bankacounttransaction;

import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccount;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.community.transactionpost.Status;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BankAccountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankAccountTransactionId;

    @Column
    private Code code;
    private int amount;
    private Status status;

    @ManyToOne
    @JoinColumn(name = "receiver_bank_account_id")
    private UserBankAccount receiverBankAccount;

    @ManyToOne
    @JoinColumn(name= "sender_bank_account_id")
    private UserBankAccount senderBankAccount;

    @ManyToOne
    @JoinColumn(name = "branch_bank_account_id")
    private BranchBankAccount branchBankAccount;



}
