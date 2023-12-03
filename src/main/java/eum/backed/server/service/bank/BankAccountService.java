package eum.backed.server.service.bank;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.controller.bank.dto.response.BankAccountResponseDTO;
import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.bank.bankacounttransaction.Code;
import eum.backed.server.domain.bank.bankacounttransaction.Status;
import eum.backed.server.domain.bank.bankacounttransaction.TrasnactionType;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccount;
import eum.backed.server.domain.bank.branchbankaccount.BranchBankAccountRepository;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.bank.userbankaccount.UserBankAccountRepository;
import eum.backed.server.domain.community.chat.ChatRoom;
import eum.backed.server.domain.community.chat.ChatRoomRepository;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankAccountService {
    private final UserBankAccountRepository userBankAccountRepository;
    private final BankTransactionService bankTransactionService;
    private final BranchBankAccountRepository branchBankAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final UsersRepository usersRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MarketPostRepository marketPostRepository;
    //일반 유저 계정 생성
    private UserBankAccount createUserBankAccount(String cardName, String password,Users user){
        UserBankAccount userBankAccount = UserBankAccount.toEntity(cardName,passwordEncoder.encode(password),user);
        UserBankAccount savedUserBankAccount =  userBankAccountRepository.save(userBankAccount);
        BranchBankAccount initialBankAccount = branchBankAccountRepository.findById(1L).get(); //초기 300 포인트 제공 계좌

        userBankAccount.deposit(300L);
        UserBankAccount UpdatedBankAccount= userBankAccountRepository.save(userBankAccount);

        BankTransactionDTO.Transaction transaction = BankTransactionDTO.toInitialDTO(Code.SUCCESS, Status.INITIAL, 300L, savedUserBankAccount, initialBankAccount);
        bankTransactionService.createTransactionWithBranchBank(transaction);
        return UpdatedBankAccount;

    }
    public APIResponse updatePassword(BankAccountRequestDTO.UpdatePassword updatePassword, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        UserBankAccount myBankAccount = getUser.getUserBankAccount();
        if(!passwordEncoder.matches(updatePassword.getCurrentPassword(),getUser.getUserBankAccount().getPassword())) throw new IllegalArgumentException("잘못된 비밀번호");
        myBankAccount.updatePassword(passwordEncoder.encode(updatePassword.getNewPassword()));
        userBankAccountRepository.save(myBankAccount);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, "비밀번호 업데이트");
    }


    public APIResponse remittance(BankAccountRequestDTO.Remittance remittance, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        Profile getProfile = profileRepository.findByNickname(remittance.getNickname()).orElseThrow(() -> new IllegalArgumentException("Invalid nickname"));
        Users receiver = getProfile.getUser();
        if(!passwordEncoder.matches(remittance.getPassword(),getUser.getUserBankAccount().getPassword())) throw new IllegalArgumentException("잘못된 비밀번호");
        UserBankAccount myBankAccount = getUser.getUserBankAccount();
        UserBankAccount receiverBankAccount = userBankAccountRepository.findByUser(receiver).orElseThrow(() -> new NullPointerException("InValid receiver"));
        //각 계좌에 송금 결과 반영
        remittance(myBankAccount, receiverBankAccount, remittance.getAmount());
        //거래 로그 작성
        BankTransactionDTO.Transaction myTransaction = BankTransactionDTO.toUserTransactionDTO(Code.SUCCESS, Status.TRADING, TrasnactionType.WITHDRAW, remittance.getAmount(), myBankAccount, null,receiverBankAccount);
        BankTransactionDTO.Transaction opponentTransaction = BankTransactionDTO.toUserTransactionDTO(Code.SUCCESS, Status.TRADING,TrasnactionType.DEPOSIT, remittance.getAmount(), receiverBankAccount,myBankAccount,null);

        bankTransactionService.createTransactionWithUserBankAccount(myTransaction);
        bankTransactionService.createTransactionWithUserBankAccount(opponentTransaction);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, "송금 성공");
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


    public BankTransactionDTO.UpdateTotalSunrise remittanceByChat(String password,Long ChatRoomId, String email) {
        ChatRoom getChatRoom = chatRoomRepository.findById(ChatRoomId).orElseThrow(() -> new NullPointerException("Invalid id"));
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        BankTransactionDTO.TransactionUser transactionUser = checkSender(getChatRoom, getUser);
        UserBankAccount myBankAccount = transactionUser.getSender().getUserBankAccount();
        UserBankAccount receiverAccount = transactionUser.getReceiver().getUserBankAccount();

        if(!passwordEncoder.matches(password,getUser.getUserBankAccount().getPassword())) throw new IllegalArgumentException("잘못된 비밀번호");

        MarketPost marketPost = getChatRoom.getMarketPost();
        Long amount = marketPost.getPay();
//        송금 결과 각 계좌 반영
        remittance(myBankAccount, receiverAccount, amount);
//        각 거래 로그 작성
        BankTransactionDTO.Transaction myTransaction = BankTransactionDTO.toUserTransactionDTO(Code.SUCCESS, Status.TRADING, TrasnactionType.WITHDRAW, amount, myBankAccount, null,receiverAccount);
        BankTransactionDTO.Transaction opponentTransaction = BankTransactionDTO.toUserTransactionDTO(Code.SUCCESS, Status.TRADING,TrasnactionType.DEPOSIT, amount, receiverAccount,myBankAccount,null);
        bankTransactionService.createTransactionWithUserBankAccount(myTransaction);
        bankTransactionService.createTransactionWithUserBankAccount(opponentTransaction);
        marketPost.updateStatus(eum.backed.server.domain.community.marketpost.Status.TRANSACTION_COMPLETED);
        marketPostRepository.save(marketPost);
        return BankTransactionDTO.UpdateTotalSunrise.builder().me(getUser).receiver(receiverAccount.getUser()).amount(amount).build();



    }
//    도움 제공, 요청에 따른 송금 자 설정 예외처리
    private BankTransactionDTO.TransactionUser checkSender(ChatRoom chatRoom, Users user){
//        true인경우 도움 요청, 작성자가 송금
        if(chatRoom.getMarketPost().getMarketType()==MarketType.REQUEST_HELP){
            Users sender = chatRoom.getPostWriter();
            Users receiver = chatRoom.getApplicant();
            if(user !=sender) throw new IllegalArgumentException("송금해야할 유저가 잘못되었습니다");
            log.info(String.valueOf((user !=sender)));
            return BankTransactionDTO.TransactionUser.builder().sender(sender).receiver(receiver).build();
        }
        Users sender = chatRoom.getApplicant();
        Users receiver = chatRoom.getPostWriter();
        if(user !=sender) throw new IllegalArgumentException("송금해야할 유저가 잘못되었습니다");
        return BankTransactionDTO.TransactionUser.builder().sender(sender).receiver(receiver).build();
    }

    public APIResponse<BankAccountResponseDTO.AccountInfo> getOtherAccountInfo(BankAccountRequestDTO.CheckNickName checkNickName) {
        Profile receiverProfile = profileRepository.findByNickname(checkNickName.getNickname()).orElseThrow(() -> new IllegalArgumentException("없는 닉네임입니다"));
        String receiverCardName = receiverProfile.getUser().getUserBankAccount().getAccountName();
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, BankAccountResponseDTO.AccountInfo.builder().balance(null).cardName(receiverCardName).build());

    }

    public APIResponse<BankAccountResponseDTO.AccountInfo> createPassword(BankAccountRequestDTO.Password password, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        UserBankAccount userBankAccount = createUserBankAccount("", password.getPassword(),getUser);
        userBankAccountRepository.save(userBankAccount);
        getUser.updateRole(Role.ROLE_USER);
        usersRepository.save(getUser);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, BankAccountResponseDTO.AccountInfo.builder().balance(userBankAccount.getBalance()).cardName(userBankAccount.getAccountName()).build());
    }

    public APIResponse<BankAccountResponseDTO.AccountInfo> getAccountInfo(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        UserBankAccount userBankAccount = userBankAccountRepository.findByUser(getUser).orElseThrow(() -> new IllegalArgumentException("아직 비밀번호 설정이 안되있습니다"));
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, BankAccountResponseDTO.AccountInfo.builder().balance(userBankAccount.getBalance()).cardName(userBankAccount.getAccountName()).build());
    }

    public APIResponse<BankAccountResponseDTO.AccountInfo> updateCardName(String cardName, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        UserBankAccount userBankAccount = getUser.getUserBankAccount();
        userBankAccount.updateCardName(cardName);
        userBankAccountRepository.save(userBankAccount);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, BankAccountResponseDTO.AccountInfo.builder().balance(userBankAccount.getBalance()).cardName(userBankAccount.getAccountName()).build());

    }
}
