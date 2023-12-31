package eum.backed.server.service.community.bank;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.controller.bank.dto.response.BankAccountResponseDTO;
import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.community.bank.bankacounttransaction.Code;
import eum.backed.server.domain.community.bank.bankacounttransaction.Status;
import eum.backed.server.domain.community.bank.bankacounttransaction.TrasnactionType;
import eum.backed.server.domain.community.bank.branchbankaccount.BranchBankAccount;
import eum.backed.server.domain.community.bank.branchbankaccount.BranchBankAccountRepository;
import eum.backed.server.domain.community.bank.userbankaccount.UserBankAccount;
import eum.backed.server.domain.community.bank.userbankaccount.UserBankAccountRepository;
import eum.backed.server.domain.community.block.Block;
import eum.backed.server.domain.community.block.BlockRepository;
import eum.backed.server.domain.community.chat.ChatRoom;
import eum.backed.server.domain.community.chat.ChatRoomRepository;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.service.community.bank.DTO.BankTransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
    private final BlockRepository blockRepository;
    //일반 유저 계정 생성
    private UserBankAccount createUserBankAccount(String cardName, String password,Users user){
        UserBankAccount userBankAccount = UserBankAccount.toEntity(cardName,passwordEncoder.encode(password),user);
        UserBankAccount savedUserBankAccount =  userBankAccountRepository.save(userBankAccount);
        BranchBankAccount initialBankAccount = branchBankAccountRepository.findById(1L).get(); //초기 300 포인트 제공 계좌
        Long amount = (user.getRole() != Role.ROLE_ORGANIZATION) ? 300L : 100000L;
        userBankAccount.deposit(amount);
        UserBankAccount UpdatedBankAccount= userBankAccountRepository.save(userBankAccount);

        BankTransactionDTO.Transaction transaction = BankTransactionDTO.toInitialDTO(Code.SUCCESS, Status.INITIAL, amount, savedUserBankAccount, initialBankAccount);
        bankTransactionService.createTransactionWithBranchBank(transaction);
        return UpdatedBankAccount;

    }
    public APIResponse updatePassword(BankAccountRequestDTO.Password password, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        UserBankAccount myBankAccount = getUser.getUserBankAccount();
        myBankAccount.updatePassword(passwordEncoder.encode(password.getPassword()));
        userBankAccountRepository.save(myBankAccount);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, "비밀번호 업데이트");
    }


    public APIResponse remittance(BankAccountRequestDTO.Remittance remittance, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        Profile getProfile = profileRepository.findByNickname(remittance.getNickname()).orElseThrow(() -> new IllegalArgumentException("Invalid nickname"));
        Users receiver = getProfile.getUser();

        checkBlocked(getUser,receiver);
        checkWithdrawal(receiver);
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

            checkBlocked(sender,receiver);
            checkWithdrawal(receiver);
            if(user !=sender) throw new IllegalArgumentException("송금해야할 유저가 잘못되었습니다");
            log.info(String.valueOf((user !=sender)));
            return BankTransactionDTO.TransactionUser.builder().sender(sender).receiver(receiver).build();
        }
        Users sender = chatRoom.getApplicant();
        Users receiver = chatRoom.getPostWriter();

        checkBlocked(sender,receiver);
        checkWithdrawal(receiver);
        if(user !=sender) throw new IllegalArgumentException("송금해야할 유저가 잘못되었습니다");
        return BankTransactionDTO.TransactionUser.builder().sender(sender).receiver(receiver).build();
    }

    public APIResponse<BankAccountResponseDTO.AccountInfo> getOtherAccountInfo(BankAccountRequestDTO.CheckNickName checkNickName,String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        Profile receiverProfile = profileRepository.findByNickname(checkNickName.getNickname()).orElseThrow(() -> new IllegalArgumentException("없는 닉네임입니다"));
        checkBlocked(getUser,receiverProfile.getUser());
        checkWithdrawal(receiverProfile.getUser());
        String receiverCardName = receiverProfile.getUser().getUserBankAccount().getAccountName();
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, BankAccountResponseDTO.AccountInfo.builder().balance(null).cardName(receiverCardName).build());

    }

    public APIResponse<BankAccountResponseDTO.AccountInfo> createPassword(BankAccountRequestDTO.Password password, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        if(userBankAccountRepository.existsByUser(getUser)) throw new IllegalArgumentException("이미 비밀번호를 생성했습니다");
        UserBankAccount userBankAccount = createUserBankAccount("", password.getPassword(),getUser);
        userBankAccountRepository.save(userBankAccount);
        Role role = (getUser.getRole() == Role.ROLE_ORGANIZATION) ? Role.ROLE_ORGANIZATION : Role.ROLE_USER;
        getUser.updateRole(role);
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

    public APIResponse validatePassword(BankAccountRequestDTO.Password password, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        if(!passwordEncoder.matches(password.getPassword(),getUser.getUserBankAccount().getPassword())) throw new IllegalArgumentException("잘못된 비밀번호");
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, "validate password success");

    }
//    계좌 동결

    public void freezeAccount(Users getUser) {
        if(!userBankAccountRepository.existsByUser(getUser)){
            return ;
        }
        UserBankAccount userBankAccount = getUser.getUserBankAccount();
        userBankAccount.updateFreeze(true);
    }
    private void checkWithdrawal(Users user){
        if(user.isDeleted()) throw new IllegalArgumentException("이미 탈퇴한 회원입니다");
    }
    private void checkBlocked(Users opponent ,Users user){
        if(blockRepository.existsByBlockerAndBlocked(opponent,user) || blockRepository.existsByBlockerAndBlocked(user,opponent)){
            throw new IllegalArgumentException("차단 했거나 차단 당한 유저입니다");
        }
    }


}
