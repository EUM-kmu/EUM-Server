package eum.backed.server.controller.bank;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.bank.DTO.request.BankAccountRequestDTO;
import eum.backed.server.controller.bank.DTO.response.BankAccountResponseDTO;
import eum.backed.server.domain.auth.CustomUserDetails;
import eum.backed.server.domain.bank.bankacounttransaction.TransactionType;
import eum.backed.server.service.bank.BankAccountService;
import eum.backed.server.service.bank.BankTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bank-account")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final BankTransactionService bankTransactionService;

    /**
     * 내 계죄 정보 조회
     * @param customUserDetails
     * @return : 계좌 카드 이름, 잔액
     */
    @GetMapping
    public ResponseEntity<APIResponse<BankAccountResponseDTO.AccountInfo>> getAccountInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(bankAccountService.getAccountInfo(Long.valueOf(customUserDetails.getUsername())));
    }

    /**
     * 다른 유저 계좌 조회(송금확인)
     * @param checkNickName : 송금할 유저의 닉네임
     * @param customUserDetails
     * @return
     */
    @PostMapping("/other")
    public ResponseEntity<APIResponse<BankAccountResponseDTO.AccountInfo>> getOtherAccountInfo(@RequestBody @Validated BankAccountRequestDTO.CheckNickName checkNickName ,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(bankAccountService.getOtherAccountInfo(checkNickName,Long.valueOf(customUserDetails.getUsername())));
    }

    /**
     *
     * @param cardName : 계좌이름 설정
     * @param customUserDetails
     * @return
     */
    @PutMapping
    public ResponseEntity<APIResponse<BankAccountResponseDTO.AccountInfo>> updateCardname(@RequestBody  BankAccountRequestDTO.CardName cardName,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(bankAccountService.updateCardName(cardName.getCardName(), Long.valueOf(customUserDetails.getUsername())));
    }

    /**
     * 카드 비밀번호 설정 시 계좌 자동 생성
     * @param password : 비밀번호
     * @param customUserDetails
     * @return
     */
    @PostMapping("/password")
    public ResponseEntity<APIResponse<BankAccountResponseDTO.AccountInfo>> createPassword(@RequestBody @Validated BankAccountRequestDTO.Password password,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return new ResponseEntity<>(bankAccountService.createPassword(password,Long.valueOf(customUserDetails.getUsername())), HttpStatus.CREATED);
    }

    /**
     * 비밀번호 확인
     * @param password
     * @param customUserDetails
     * @return
     */
    @PostMapping("/validate")
    public ResponseEntity<APIResponse> validatePassword(@RequestBody @Validated BankAccountRequestDTO.Password password,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return new ResponseEntity<>(bankAccountService.validatePassword(password, Long.valueOf(customUserDetails.getUsername())), HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<APIResponse> updatePassword(@RequestBody @Validated BankAccountRequestDTO.Password password, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(bankAccountService.updatePassword(password,Long.valueOf(customUserDetails.getUsername())));
    }
    @PostMapping("/remittance")
    public ResponseEntity<APIResponse> remittance(@RequestBody @Validated  BankAccountRequestDTO.Remittance remittance, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(bankAccountService.remittance(remittance, Long.valueOf(customUserDetails.getUsername())));
    }
    @GetMapping("/pay")
    public ResponseEntity<APIResponse<List<BankAccountResponseDTO.HistoryWithInfo>>> getAllHistory(@RequestParam(name = "type",required = false) TransactionType transactionType, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(bankTransactionService.getAllHistory(Long.valueOf(customUserDetails.getUsername()),transactionType));
    }


}
