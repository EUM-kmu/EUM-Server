package eum.backed.server.domain.bank.bankacounttransaction;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccount;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BankAccountTransaction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankAccountTransactionId;

    @Column
    private Long amount;
    private Long myCurrentBalance;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column
    @Enumerated(EnumType.STRING)
    private Code code;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "my_bank_account")
    private UserBankAccount myBankAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_bank_account_id")
    private UserBankAccount receiverBankAccount;

    @ManyToOne
    @JoinColumn(name= "sender_bank_account_id")
    private UserBankAccount senderBankAccount;

    @ManyToOne
    @JoinColumn(name = "branch_bank_account_id")
    private BranchBankAccount branchBankAccount;

    public static BankAccountTransaction toEntity(Long amount, Code code, Status status, TransactionType transactionType, UserBankAccount myBankAccount, UserBankAccount receiverBankAccount, UserBankAccount senderBankAccount, BranchBankAccount branchBankAccount){
        return BankAccountTransaction.builder()
                .amount(amount)
                .code(code)
                .status(status)
                .transactionType(transactionType)
                .myBankAccount(myBankAccount)
                .myCurrentBalance(myBankAccount.getBalance())
                .receiverBankAccount(receiverBankAccount)
                .senderBankAccount(senderBankAccount)
                .branchBankAccount(branchBankAccount).build();
    }



}
