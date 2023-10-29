package eum.backed.server.service.bank.DTO;

import eum.backed.server.domain.bank.bankacounttransaction.Code;
import eum.backed.server.domain.bank.bankacounttransaction.Status;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccount;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class BankTransactionDTO {
    @Getter
    @Setter
    @Builder
    public static class Transaction {
        private Code code;
        private Status status;
        private int amount;
        private UserBankAccount receiverBankAccount;
        private UserBankAccount senderBankAccount;
        private BranchBankAccount branchBankAccount;
    }

    public static Transaction toInitialDTO(Code code, Status status, int amount, UserBankAccount userBankAccount, BranchBankAccount branchBankAccount){
        return Transaction.builder()
                .status(status)
                .code(code)
                .amount(amount)
                .receiverBankAccount(userBankAccount)
                .branchBankAccount(branchBankAccount)
                .build();
    }

    public static Transaction toUserTransactionDTO (Code code, Status status, int amount, UserBankAccount senderBankAccount, UserBankAccount receiverBankAccount){
        return Transaction.builder()
                .status(status)
                .code(code)
                .amount(amount)
                .receiverBankAccount(receiverBankAccount)
                .senderBankAccount(senderBankAccount)
                .build();
    }
}
