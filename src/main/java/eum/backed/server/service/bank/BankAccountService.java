package eum.backed.server.service.bank;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.bank.dto.TransactionRequestDTO;
import eum.backed.server.domain.bank.bankacounttransaction.Code;
import eum.backed.server.domain.bank.bankacounttransaction.Status;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccount;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccountRepository;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccountRepository;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final UserBankAccountRepository userBankAccountRepository;
    private final BankTransactionService bankTransactionService;
    private final BranchBankAccountRepository branchBankAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final UsersRepository usersRepository;
    //일반 유저 계정 생성
    public void createUserBankAccount(String nickname, String password,Users user){
        UserBankAccount userBankAccount = UserBankAccount.toEntity(nickname,passwordEncoder.encode(password),user);
        UserBankAccount savedUserBankAccount =  userBankAccountRepository.save(userBankAccount);
        BranchBankAccount initialBankAccount = branchBankAccountRepository.findById(1L).get(); //초기 300 포인트 제공 계좌

        userBankAccount.deposit(300);
        userBankAccountRepository.save(userBankAccount);

        BankTransactionDTO.Transaction transaction = BankTransactionDTO.toInitialDTO(Code.SUCCESS, Status.INITIAL, 300, savedUserBankAccount, initialBankAccount);
        bankTransactionService.createTransactionWithBranchBank(transaction);
    }


    public DataResponse remittance(TransactionRequestDTO.Remittance remittance, String email) {
        Users sender = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        Profile getProfile = profileRepository.findByNickname(remittance.getReceiverNickname()).orElseThrow(() -> new IllegalArgumentException("Invalid nickname"));
        Users receiver = getProfile.getUser();

        UserBankAccount senderBankAccount = userBankAccountRepository.findByUser(sender).orElseThrow(() -> new NullPointerException("InValid sender"));
        UserBankAccount receiverBankAccount = userBankAccountRepository.findByUser(receiver).orElseThrow(() -> new NullPointerException("InValid receiver"));
        //각 계좌에 송금 결과 반영
        remittance(senderBankAccount, receiverBankAccount, remittance.getAmount());
        //거래 로그 작성
        BankTransactionDTO.Transaction transaction = BankTransactionDTO.toUserTransactionDTO(Code.SUCCESS, Status.TRADING, remittance.getAmount(), senderBankAccount, receiverBankAccount);
        bankTransactionService.createTransactionWithUserBankAccount(transaction);
        return new DataResponse<>().success("송금 성공");
    }
    private void remittance(UserBankAccount senderBankAccount,UserBankAccount receiverAccount, int amount){
        if(senderBankAccount.getBalance() < amount){
            throw new IllegalArgumentException("출금 예산을 초과했습니다");
        }
        senderBankAccount.withDraw(amount);
        receiverAccount.deposit(amount);
        userBankAccountRepository.save(senderBankAccount);
        userBankAccountRepository.save(receiverAccount);

    }



}
