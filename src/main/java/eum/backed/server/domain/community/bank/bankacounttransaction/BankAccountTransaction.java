package eum.backed.server.domain.community.bank.bankacounttransaction;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.bank.branchbankaccount.BranchBankAccount;
import eum.backed.server.domain.community.bank.userbankaccount.UserBankAccount;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
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
    private TrasnactionType trasnactionType;

    @Column
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Code", allowableValues = "SUCCESS, FAIL")
    private Code code;

    @Column
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Status", allowableValues = "INITIAL, TRADING, REFUND")
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

    public static BankAccountTransaction toEntity(Long amount, Code code, Status status,TrasnactionType trasnactionType,UserBankAccount myBankAccount, UserBankAccount receiverBankAccount, UserBankAccount senderBankAccount, BranchBankAccount branchBankAccount){
        return BankAccountTransaction.builder()
                .amount(amount)
                .code(code)
                .status(status)
                .trasnactionType(trasnactionType)
                .myBankAccount(myBankAccount)
                .myCurrentBalance(myBankAccount.getBalance())
                .receiverBankAccount(receiverBankAccount)
                .senderBankAccount(senderBankAccount)
                .branchBankAccount(branchBankAccount).build();
    }



}
