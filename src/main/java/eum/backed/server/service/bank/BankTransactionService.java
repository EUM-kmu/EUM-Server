package eum.backed.server.service.bank;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.bank.dto.response.BankAccountResponseDTO;
import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransaction;
import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransactionRepository;
import eum.backed.server.domain.bank.bankacounttransaction.TrasnactionType;
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
    public void createTransactionWithUserBankAccount(BankTransactionDTO.Transaction bankTransactionDTO){
        BankAccountTransaction bankAccountTransaction = BankAccountTransaction.toEntity(bankTransactionDTO.getAmount(), bankTransactionDTO.getCode(),bankTransactionDTO.getStatus(),bankTransactionDTO.getTrasnactionType(),bankTransactionDTO.getMyBankAccount(),bankTransactionDTO.getReceiverBankAccount(),bankTransactionDTO.getSenderBankAccount(),null);
        bankAccountTransactionRepository.save(bankAccountTransaction);
    }
    public void createTransactionWithBranchBank(BankTransactionDTO.Transaction bankTransactionDTO){
        UserBankAccount myBankAccount = bankTransactionDTO.getMyBankAccount();
        BankAccountTransaction bankAccountTransaction = BankAccountTransaction.toEntity(bankTransactionDTO.getAmount(), bankTransactionDTO.getCode(),bankTransactionDTO.getStatus(),bankTransactionDTO.getTrasnactionType(),myBankAccount,null,null,bankTransactionDTO.getBranchBankAccount());
        bankAccountTransactionRepository.save(bankAccountTransaction);

    }
    public APIResponse<List<BankAccountResponseDTO.GetAllHistory>> getAllHistory(String email, TrasnactionType trasnactionType) {
        Users geUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        UserBankAccount bankAccount = userBankAccountRepository.findByUser(geUser).orElseThrow(() -> new NullPointerException("Invalid user"));
        if (trasnactionType == TrasnactionType.DEPOSIT || trasnactionType == TrasnactionType.WITHDRAW){
            List<BankAccountTransaction> bankAccountTransactions = bankAccountTransactionRepository.findByMyBankAccountAndTrasnactionTypeOrderByCreateDateDesc(bankAccount,trasnactionType).orElse(Collections.emptyList());
            List<BankAccountResponseDTO.GetAllHistory> getAllHistories = getAllHistories(bankAccountTransactions);
            return APIResponse.of(SuccessCode.SELECT_SUCCESS, getAllHistories);
        }
        List<BankAccountTransaction> bankAccountTransactions = bankAccountTransactionRepository.findByMyBankAccountOrderByCreateDateDesc(bankAccount).orElse(Collections.emptyList());
        List<BankAccountResponseDTO.GetAllHistory> getAllHistories = getAllHistories(bankAccountTransactions);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, getAllHistories);
    }
    private List<BankAccountResponseDTO.GetAllHistory> getAllHistories(List<BankAccountTransaction> bankAccountTransactions){
        List<BankAccountResponseDTO.GetAllHistory> allHistories = new ArrayList<>();
        for(BankAccountTransaction bankAccountTransaction: bankAccountTransactions){
            BankAccountResponseDTO.GetAllHistory getAllHistory = bankAccountResponseDTO.newGetAllHistory(bankAccountTransaction);
            allHistories.add(getAllHistory);
        }
        return allHistories;
    }


}
