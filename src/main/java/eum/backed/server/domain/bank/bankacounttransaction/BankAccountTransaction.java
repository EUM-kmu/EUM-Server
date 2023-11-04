package eum.backed.server.domain.bank.bankacounttransaction;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccount;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import eum.backed.server.service.bank.BankAccountService;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
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
    private int amount;

    @Column
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Code", allowableValues = "SUCCESS, FAIL")
    private Code code;

    @Column
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Status", allowableValues = "INITIAL, TRADING, REFUND")
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

    public static BankAccountTransaction toEntity(int amount, Code code, Status status, UserBankAccount receiverBankAccount, UserBankAccount senderBankAccount, BranchBankAccount branchBankAccount){
        return BankAccountTransaction.builder()
                .amount(amount)
                .code(code)
                .status(status)
                .receiverBankAccount(receiverBankAccount)
                .senderBankAccount(senderBankAccount)
                .branchBankAccount(branchBankAccount).build();
    }



}
