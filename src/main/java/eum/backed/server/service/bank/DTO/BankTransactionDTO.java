package eum.backed.server.service.bank.DTO;

import eum.backed.server.domain.bank.bankacounttransaction.Code;
import eum.backed.server.domain.bank.bankacounttransaction.Status;
import eum.backed.server.domain.bank.bankacounttransaction.TrasnactionType;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccount;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.community.user.Users;
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
        private Long amount;
        private TrasnactionType trasnactionType;
        private UserBankAccount myBankAccount;
        private UserBankAccount receiverBankAccount;
        private UserBankAccount senderBankAccount;
        private BranchBankAccount branchBankAccount;
    }
    @Getter
    @Setter
    @Builder
    public static class TransactionUser {
        private Users sender;
        private Users receiver;
    }

    @Getter
    @Setter
    @Builder
    public static class UpdateTotalSunrise{
        private Users me;
        private Users receiver;
        private Long amount;
    }

    public static Transaction toInitialDTO(Code code, Status status, Long amount, UserBankAccount userBankAccount, BranchBankAccount branchBankAccount){
        return Transaction.builder()
                .status(status)
                .trasnactionType(TrasnactionType.DEPOSIT)
                .code(code)
                .amount(amount)
                .myBankAccount(userBankAccount)
                .branchBankAccount(branchBankAccount)
                .build();
    }

    public static Transaction toUserTransactionDTO (Code code, Status status, TrasnactionType trasnactionType,Long amount,UserBankAccount myBankAccount, UserBankAccount senderBankAccount, UserBankAccount receiverBankAccount){
        return Transaction.builder()
                .status(status)
                .code(code)
                .trasnactionType(trasnactionType)
                .amount(amount)
                .myBankAccount(myBankAccount)
                .receiverBankAccount(receiverBankAccount)
                .senderBankAccount(senderBankAccount)
                .build();
    }
}
