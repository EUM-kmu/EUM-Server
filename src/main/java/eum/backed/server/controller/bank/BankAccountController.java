package eum.backed.server.controller.bank;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.controller.bank.dto.response.BankAccountResponseDTO;
import eum.backed.server.domain.bank.bankacounttransaction.TrasnactionType;
import eum.backed.server.service.bank.BankAccountService;
import eum.backed.server.service.bank.BankTransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankAccount")
@RequiredArgsConstructor
@Api(tags = "계좌관련 api")
@Slf4j
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final BankTransactionService bankTransactionService;

    @PostMapping("/updatePassword")
    @ApiOperation(value = "카드 비밀 번호 바꾸기")
    public DataResponse updatePassword(@RequestBody BankAccountResponseDTO.UpdatePassword updatePassword,@AuthenticationPrincipal String email){
        return bankAccountService.updatePassword(updatePassword.getPassword(),email);
    }
    @PostMapping("/remittance")
    @ApiOperation(value = "거래(송금하기)")
    public DataResponse remittance(@RequestBody BankAccountRequestDTO.Remittance remittance, @AuthenticationPrincipal String email){
        return bankAccountService.remittance(remittance, email);
    }
    @GetMapping("/getAllHistory")
    @ApiOperation(value = "거래 내역 조회")
    public DataResponse<List<BankAccountResponseDTO.GetAllHistory>> getAllHistory(@AuthenticationPrincipal String email){
        return bankTransactionService.getAllHistory(email,null);
    }
    @GetMapping("/getDePositHistory")
    @ApiOperation(value = "입금 내역 조회")
    public DataResponse<List<BankAccountResponseDTO.GetAllHistory>> getDePositHistory(@AuthenticationPrincipal String email){
        return bankTransactionService.getAllHistory(email, TrasnactionType.DEPOSIT);
    }
    @GetMapping("/getWithDrawHistory")
    @ApiOperation(value = "출금 내역 조회")
    public DataResponse<List<BankAccountResponseDTO.GetAllHistory>> getWithDrawHistory(@AuthenticationPrincipal String email){
        return bankTransactionService.getAllHistory(email,TrasnactionType.WITHDRAW);
    }
}
