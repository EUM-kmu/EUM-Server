package eum.backed.server.service.bank;

import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransaction;
import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransactionRepository;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccountRepository;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankTransactionService {
    private final BankAccountTransactionRepository bankAccountTransactionRepository;
    public void createTransactionWithUserBankAccount(BankTransactionDTO.Transaction bankTransactionDTO){
        BankAccountTransaction bankAccountTransaction = BankAccountTransaction.toEntity(bankTransactionDTO.getAmount(), bankTransactionDTO.getCode(),bankTransactionDTO.getStatus(),bankTransactionDTO.getReceiverBankAccount(),bankTransactionDTO.getSenderBankAccount(),null);
        bankAccountTransactionRepository.save(bankAccountTransaction);
    }
    public void createTransactionWithBranchBank(BankTransactionDTO.Transaction bankTransactionDTO){
        UserBankAccount userBankAccount = bankTransactionDTO.getReceiverBankAccount();
        BankAccountTransaction bankAccountTransaction = BankAccountTransaction.toEntity(bankTransactionDTO.getAmount(), bankTransactionDTO.getCode(),bankTransactionDTO.getStatus(),userBankAccount,null,bankTransactionDTO.getBranchBankAccount());
        bankAccountTransactionRepository.save(bankAccountTransaction);

    }
}
