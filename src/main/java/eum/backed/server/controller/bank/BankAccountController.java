package eum.backed.server.controller.bank;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.controller.bank.dto.response.BankAccountResponseDTO;
import eum.backed.server.domain.bank.bankacounttransaction.TrasnactionType;
import eum.backed.server.service.bank.BankAccountService;
import eum.backed.server.service.bank.BankTransactionService;
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
    @ApiOperation("계좌 정보 조회")
    public ResponseEntity<APIResponse<BankAccountResponseDTO.AccountInfo>> getAccountInfro(@AuthenticationPrincipal String email){
        return ResponseEntity.ok(bankAccountService.getAccountInfo(email));
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

    @PutMapping("/password")
    @ApiOperation(value = "카드 비밀 번호 바꾸기")
    public ResponseEntity<APIResponse> updatePassword(@RequestBody @Validated BankAccountRequestDTO.UpdatePassword updatePassword, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(bankAccountService.updatePassword(updatePassword,email));
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
    @PostMapping("/check")
    @ApiOperation(value = "닉네임 확인 및 잔액 확인",notes = "송금 시 잔액 확인")
    public ResponseEntity<APIResponse<BankAccountResponseDTO.CheckNickName>> checkNickName(@RequestBody @Validated BankAccountRequestDTO.CheckNickName checkNickName,@AuthenticationPrincipal String email ){
        return ResponseEntity.ok(bankAccountService.checkNickName(checkNickName, email));
    }

}
