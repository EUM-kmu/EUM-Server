package eum.backed.server.controller.bank;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.controller.bank.dto.response.BankAccountResponseDTO;
import eum.backed.server.domain.bank.bankacounttransaction.TrasnactionType;
import eum.backed.server.service.community.bank.BankAccountService;
import eum.backed.server.service.community.bank.BankTransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank-account")
@RequiredArgsConstructor
@Api(tags = "bank account ")
@Slf4j
@CrossOrigin("*")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final BankTransactionService bankTransactionService;

    @GetMapping
    @ApiOperation("내 계좌 정보 조회")
    public ResponseEntity<APIResponse<BankAccountResponseDTO.AccountInfo>> getAccountInfo(@AuthenticationPrincipal String email){
        return ResponseEntity.ok(bankAccountService.getAccountInfo(email));
    }
    @PostMapping("/other")
    @ApiOperation(value = "닉네임 별 조회")
    public ResponseEntity<APIResponse<BankAccountResponseDTO.AccountInfo>> getOtherAccountInfo(@RequestBody @Validated BankAccountRequestDTO.CheckNickName checkNickName ){
        return ResponseEntity.ok(bankAccountService.getOtherAccountInfo(checkNickName));
    }
    @PutMapping
    @ApiOperation("계좌 이름 추가")
    public ResponseEntity<APIResponse<BankAccountResponseDTO.AccountInfo>> updateCardname(@RequestBody  BankAccountRequestDTO.CardName cardName,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(bankAccountService.updateCardName(cardName.getCardName(), email));
    }

    @PostMapping("/password")
    @ApiOperation(value = "카드 비밀번호 생성")
    public ResponseEntity<APIResponse<BankAccountResponseDTO.AccountInfo>> createPassword(@RequestBody @Validated BankAccountRequestDTO.Password password,@AuthenticationPrincipal String email){
        return new ResponseEntity<>(bankAccountService.createPassword(password,email), HttpStatus.CREATED);
    }
    @PostMapping("/validate")
    @ApiOperation(value = "카드 비밀번호 확인")
    public ResponseEntity<APIResponse> validatePassword(@RequestBody @Validated BankAccountRequestDTO.Password password,@AuthenticationPrincipal String email){
        return new ResponseEntity<>(bankAccountService.validatePassword(password, email), HttpStatus.OK);
    }

    @PutMapping("/password")
    @ApiOperation(value = "카드 비밀 번호 바꾸기")
    public ResponseEntity<APIResponse> updatePassword(@RequestBody @Validated BankAccountRequestDTO.Password password, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(bankAccountService.updatePassword(password,email));
    }
    @PostMapping("/remittance")
    @ApiOperation(value = "거래(송금하기)")
    public ResponseEntity<APIResponse> remittance(@RequestBody @Validated  BankAccountRequestDTO.Remittance remittance, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(bankAccountService.remittance(remittance, email));
    }
    @GetMapping("/pay")
    @ApiOperation(value = "거래 내역 조회",notes = "transactionType 별 전체 입출금 필터")
    public ResponseEntity<APIResponse<List<BankAccountResponseDTO.HistoryWithInfo>>> getAllHistory(@RequestParam(name = "type",required = false) TrasnactionType transactionType, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(bankTransactionService.getAllHistory(email,transactionType));
    }


}
