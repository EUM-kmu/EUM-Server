package eum.backed.server.service.bank;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.controller.bank.dto.response.BankAccountResponseDTO;
import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransaction;
import eum.backed.server.domain.bank.bankacounttransaction.Code;
import eum.backed.server.domain.bank.bankacounttransaction.Status;
import eum.backed.server.domain.bank.bankacounttransaction.TrasnactionType;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccount;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccountRepository;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccountRepository;
import eum.backed.server.domain.community.chat.ChatRoom;
import eum.backed.server.domain.community.chat.ChatRoomRepository;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final UserBankAccountRepository userBankAccountRepository;
    private final BankTransactionService bankTransactionService;
    private final BranchBankAccountRepository branchBankAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final UsersRepository usersRepository;
    private final ChatRoomRepository chatRoomRepository;
    //일반 유저 계정 생성
    public void createUserBankAccount(String nickname, String password,Users user){
        UserBankAccount userBankAccount = UserBankAccount.toEntity(nickname,passwordEncoder.encode(password),user);
        UserBankAccount savedUserBankAccount =  userBankAccountRepository.save(userBankAccount);
        BranchBankAccount initialBankAccount = branchBankAccountRepository.findById(1L).get(); //초기 300 포인트 제공 계좌

        userBankAccount.deposit(300L);
        userBankAccountRepository.save(userBankAccount);

        BankTransactionDTO.Transaction transaction = BankTransactionDTO.toInitialDTO(Code.SUCCESS, Status.INITIAL, 300L, savedUserBankAccount, initialBankAccount);
        bankTransactionService.createTransactionWithBranchBank(transaction);
    }
    public DataResponse updatePassword(String password, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        UserBankAccount myBankAccount = getUser.getUserBankAccount();
        myBankAccount.updatePassword(passwordEncoder.encode(password));
        userBankAccountRepository.save(myBankAccount);
        return new DataResponse().success("비밀번호 변경완료");
    }


    public DataResponse remittance(BankAccountRequestDTO.Remittance remittance, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        Profile getProfile = profileRepository.findByNickname(remittance.getReceiverNickname()).orElseThrow(() -> new IllegalArgumentException("Invalid nickname"));
        Users receiver = getProfile.getUser();

        UserBankAccount myBankAccount = getUser.getUserBankAccount();
        UserBankAccount receiverBankAccount = userBankAccountRepository.findByUser(receiver).orElseThrow(() -> new NullPointerException("InValid receiver"));
        //각 계좌에 송금 결과 반영
        remittance(myBankAccount, receiverBankAccount, remittance.getAmount());
        //거래 로그 작성
        BankTransactionDTO.Transaction myTransaction = BankTransactionDTO.toUserTransactionDTO(Code.SUCCESS, Status.TRADING, TrasnactionType.WITHDRAW, remittance.getAmount(), myBankAccount, null,receiverBankAccount);
        BankTransactionDTO.Transaction opponentTransaction = BankTransactionDTO.toUserTransactionDTO(Code.SUCCESS, Status.TRADING,TrasnactionType.DEPOSIT, remittance.getAmount(), receiverBankAccount,myBankAccount,null);

        bankTransactionService.createTransactionWithUserBankAccount(myTransaction);
        bankTransactionService.createTransactionWithUserBankAccount(opponentTransaction);
        return new DataResponse<>().success("송금 성공");
    }
    private void remittance(UserBankAccount myBankAccount,UserBankAccount receiverAccount, Long amount){
        if(myBankAccount.getBalance() < amount){
            throw new IllegalArgumentException("출금 예산을 초과했습니다");
        }
        myBankAccount.withDraw(amount);
        receiverAccount.deposit(amount);
        userBankAccountRepository.save(myBankAccount);
        userBankAccountRepository.save(receiverAccount);

    }


    public DataResponse remittanceByChat(Long ChatRoomId, String email) {
        ChatRoom getChatRoom = chatRoomRepository.findById(ChatRoomId).orElseThrow(() -> new NullPointerException("Invalid id"));
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        UserBankAccount myBankAccount = getChatRoom.getPostWriter().getUserBankAccount();
        if(getUser.getUserBankAccount() != myBankAccount) throw new IllegalArgumentException("유저의 잘못된 채팅 송금 접근");
        UserBankAccount applicantBankAccount = getChatRoom.getApplicant().getUserBankAccount();

        Long amount = getChatRoom.getTransactionPost().getPay();
//        송금 결과 각 계좌 반영
        remittance(myBankAccount, applicantBankAccount, amount);
//        각 거래 로그 작성
        BankTransactionDTO.Transaction myTransaction = BankTransactionDTO.toUserTransactionDTO(Code.SUCCESS, Status.TRADING, TrasnactionType.WITHDRAW, amount, myBankAccount, null,applicantBankAccount);
        BankTransactionDTO.Transaction opponentTransaction = BankTransactionDTO.toUserTransactionDTO(Code.SUCCESS, Status.TRADING,TrasnactionType.DEPOSIT, amount, applicantBankAccount,myBankAccount,null);

        bankTransactionService.createTransactionWithUserBankAccount(myTransaction);
        bankTransactionService.createTransactionWithUserBankAccount(opponentTransaction);
        return new DataResponse<>().success("채팅 송금 성공");
    }
}
