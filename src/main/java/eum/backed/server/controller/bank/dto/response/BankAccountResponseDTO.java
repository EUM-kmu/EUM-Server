package eum.backed.server.controller.bank.dto.response;

import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransaction;
import eum.backed.server.domain.bank.bankacounttransaction.TrasnactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Getter
@Setter
public class BankAccountResponseDTO {

    @Getter
    @Setter
    @Builder
    public static class GetAllHistory {
        private TrasnactionType trasnactionType;
        private Long receiverId;
        private String receiverNickName;
        private Long senderId;
        private String senderNickName;
        private Long myCurrentBalance;
        private Long transactionAmount;
    }

    public GetAllHistory newGetAllHistory(BankAccountTransaction bankAccountTransaction){
        return GetAllHistory.builder()
                .trasnactionType(bankAccountTransaction.getTrasnactionType())
                .receiverId(bankAccountTransaction.getReceiverBankAccount().getUserBankAccountId())
                .receiverNickName(bankAccountTransaction.getReceiverBankAccount().getUser().getProfile().getNickname())
                .senderId(bankAccountTransaction.getSenderBankAccount().getUserBankAccountId())
                .senderNickName(bankAccountTransaction.getSenderBankAccount().getUser().getProfile().getNickname())
                .myCurrentBalance(bankAccountTransaction.getMyCurrentBalance())
                .transactionAmount(bankAccountTransaction.getAmount()).build();

    }
    @Getter
    @Setter
    public static class UpdatePassword {
        private String password;
    }
}
