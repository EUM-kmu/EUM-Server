package eum.backed.server.service.bank;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransaction;
import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransactionRepository;
import eum.backed.server.domain.bank.bankacounttransaction.Code;
import eum.backed.server.domain.bank.bankacounttransaction.Status;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccount;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccountRepository;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccountRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final UserBankAccountRepository userBankAccountRepository;
    private final BankAccountTransactionRepository bankAccountTransactionRepository;
    private final BranchBankAccountRepository branchBankAccountRepository;
    private final PasswordEncoder passwordEncoder;
    //일반 유저 계정 생성
    public void createUserBankAccount(String nickname, String password,Users user){
        UserBankAccount userBankAccount = UserBankAccount.toEntity(nickname,passwordEncoder.encode(password),user);
        UserBankAccount savedUserBankAccount =  userBankAccountRepository.save(userBankAccount);
        BranchBankAccount initialBankAccount = branchBankAccountRepository.findById(1L).get(); //초기 300 포인트 제공 계좌
        BankTransactionDTO.TransactionWithBranchBank transactionWithBranchBank = BankTransactionDTO.toDTO(Code.SUCCESS, Status.INITIAL, 300, savedUserBankAccount, initialBankAccount);
        createTransactionWithBranchBank(transactionWithBranchBank);
    }
    public void createTransactionWithBranchBank(BankTransactionDTO.TransactionWithBranchBank bankTransactionDTO){
        UserBankAccount userBankAccount = bankTransactionDTO.getReceiverBankAccount();
        BankAccountTransaction bankAccountTransaction = BankAccountTransaction.toEntity(bankTransactionDTO.getAmount(), bankTransactionDTO.getCode(),bankTransactionDTO.getStatus(),userBankAccount,null,bankTransactionDTO.getBranchBankAccount());
        bankAccountTransactionRepository.save(bankAccountTransaction);
        userBankAccount.updateBalance(bankAccountTransaction.getAmount());
        userBankAccountRepository.save(userBankAccount);
    }


}
