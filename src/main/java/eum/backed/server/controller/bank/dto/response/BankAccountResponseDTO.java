package eum.backed.server.controller.bank.dto.response;

import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransaction;
import eum.backed.server.domain.bank.bankacounttransaction.TrasnactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@Component
public class BankAccountResponseDTO {
    @Getter
    @Setter
    @Builder
    public static class CheckNickName{
        private String receiverNickName;
        private String receiverCardName;
        private Long myBalance;

    }

    @Getter
    @Setter
    @Builder
    private static class OpponentInfo{
        private String nickName;
        private String cardName;
    }


    @Getter
    public static class History {
        private TrasnactionType transactionType;
        private OpponentInfo opponentInfo;
        private Long myCurrentBalance;
        private Long amount;
        private String createdTime;


        public History(TrasnactionType trasnactionType, OpponentInfo opponentInfo, Long myCurrentBalance, Long amount, String createdTime) {
            this.transactionType = trasnactionType;
            this.opponentInfo = opponentInfo;
            this.myCurrentBalance = myCurrentBalance;
            this.amount = amount;
            this.createdTime = createdTime;
        }
    }
    @Builder
    @Getter
    @Setter
    public static class HistoryWithInfo{
        private String cardName;
        private Long balance;
        private List<History> histories;
    }
    public History newHistory(BankAccountTransaction bankAccountTransaction){
        LocalDateTime utcDateTime = LocalDateTime.parse(bankAccountTransaction.getCreateDate().toString(), DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime koreaZonedDateTime = utcDateTime.atZone(ZoneId.of("Asia/Seoul"));
        // 한국 시간대로 포맷팅
        String formattedDateTime = koreaZonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        if(bankAccountTransaction.getTrasnactionType() == TrasnactionType.WITHDRAW){
            String receiverNickName = bankAccountTransaction.getReceiverBankAccount().getUser().getProfile().getNickname();
            String cardName = bankAccountTransaction.getReceiverBankAccount().getAccountName();
            OpponentInfo receiverInfo = OpponentInfo.builder().nickName(receiverNickName).cardName(cardName).build();
            return new History(TrasnactionType.WITHDRAW, receiverInfo, bankAccountTransaction.getMyCurrentBalance(), bankAccountTransaction.getAmount(), formattedDateTime);
        }
        String senderNickName = (bankAccountTransaction.getSenderBankAccount() == null) ?  bankAccountTransaction.getBranchBankAccount().getAccountName():bankAccountTransaction.getSenderBankAccount().getUser().getProfile().getNickname();
        String cardName = (bankAccountTransaction.getSenderBankAccount() == null) ?  "":bankAccountTransaction.getSenderBankAccount().getAccountName();

        OpponentInfo receiverInfo = OpponentInfo.builder().nickName(senderNickName).cardName(cardName).build();
        return new History(TrasnactionType.DEPOSIT, receiverInfo, bankAccountTransaction.getMyCurrentBalance(), bankAccountTransaction.getAmount(), formattedDateTime);

    }
    @Builder
    @Getter
    @Setter
    public static class AccountInfo {
        private String cardName;
        private Long balance;
    }
}
