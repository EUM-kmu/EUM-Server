package eum.backed.server.service.bank;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.bank.DTO.response.BankAccountResponseDTO;
import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransaction;
import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransactionRepository;
import eum.backed.server.domain.bank.bankacounttransaction.TransactionType;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccountRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankTransactionService {
    private final BankAccountTransactionRepository bankAccountTransactionRepository;
    private final UsersRepository usersRepository;
    private final UserBankAccountRepository userBankAccountRepository;
    private final BankAccountResponseDTO bankAccountResponseDTO;

    /**
     * 유저 계좌 간 송금 거래 내역 저장
     * @param bankTransactionDTO
     */
    public void createTransactionWithUserBankAccount(BankTransactionDTO.Transaction bankTransactionDTO){
        BankAccountTransaction bankAccountTransaction = BankAccountTransaction.toEntity(bankTransactionDTO.getAmount(), bankTransactionDTO.getCode(),bankTransactionDTO.getStatus(),bankTransactionDTO.getTransactionType(),bankTransactionDTO.getMyBankAccount(),bankTransactionDTO.getReceiverBankAccount(),bankTransactionDTO.getSenderBankAccount(),null);
        bankAccountTransactionRepository.save(bankAccountTransaction);
    }

    /**
     * 중앙은행과 송금 거래내역
     * @param bankTransactionDTO
     */
    public void createTransactionWithBranchBank(BankTransactionDTO.Transaction bankTransactionDTO){
        UserBankAccount myBankAccount = bankTransactionDTO.getMyBankAccount();
        BankAccountTransaction bankAccountTransaction = BankAccountTransaction.toEntity(bankTransactionDTO.getAmount(), bankTransactionDTO.getCode(),bankTransactionDTO.getStatus(),bankTransactionDTO.getTransactionType(),myBankAccount,null,null,bankTransactionDTO.getBranchBankAccount());
        bankAccountTransactionRepository.save(bankAccountTransaction);

    }

    /**
     * 가레네약 조회
     * @param userId
     * @param transactionType DEPOSIT(입금),WITHDRAW(출금)
     * @return
     */
    public APIResponse<List<BankAccountResponseDTO.HistoryWithInfo>> getAllHistory(Long userId, TransactionType transactionType) {
        Users getUser = usersRepository.findById(userId).orElseThrow(() -> new NullPointerException("Invalid userId"));
        UserBankAccount bankAccount = userBankAccountRepository.findByUser(getUser).orElseThrow(() -> new NullPointerException("Invalid user"));
        if (!(transactionType ==null)){
            List<BankAccountTransaction> bankAccountTransactions = bankAccountTransactionRepository.findByMyBankAccountAndTransactionTypeOrderByCreateDateDesc(bankAccount, transactionType).orElse(Collections.emptyList());
            List<BankAccountResponseDTO.History> getAllHistories = getAllHistories(bankAccountTransactions);
            BankAccountResponseDTO.HistoryWithInfo historyWithInfo = BankAccountResponseDTO.HistoryWithInfo.builder().cardName(bankAccount.getAccountName()).balance(bankAccount.getBalance()).histories(getAllHistories).build();
            return APIResponse.of(SuccessCode.SELECT_SUCCESS, historyWithInfo);
        }
        List<BankAccountTransaction> bankAccountTransactions = bankAccountTransactionRepository.findByMyBankAccountOrderByCreateDateDesc(bankAccount).orElse(Collections.emptyList());
        List<BankAccountResponseDTO.History> getAllHistories = getAllHistories(bankAccountTransactions);
        BankAccountResponseDTO.HistoryWithInfo historyWithInfo = BankAccountResponseDTO.HistoryWithInfo.builder().cardName(bankAccount.getAccountName()).balance(bankAccount.getBalance()).histories(getAllHistories).build();
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, historyWithInfo);
    }

    /**
     * 전체 거래내역 dto에 저장
     * @param bankAccountTransactions
     * @return
     */
    private List<BankAccountResponseDTO.History> getAllHistories(List<BankAccountTransaction> bankAccountTransactions){
        List<BankAccountResponseDTO.History> allHistories = new ArrayList<>();
        for(BankAccountTransaction bankAccountTransaction: bankAccountTransactions){
            BankAccountResponseDTO.History history = bankAccountResponseDTO.newHistory(bankAccountTransaction);
            allHistories.add(history);
        }
        return allHistories;
    }


}
