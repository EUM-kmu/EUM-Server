package eum.backed.server.controller.bank;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.bank.dto.TransactionRequestDTO;
import eum.backed.server.service.bank.BankAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/bankAccount")
@RequiredArgsConstructor
@Slf4j
public class BankAccountController {
    private final BankAccountService bankAccountService;
    @PostMapping("/remittance")
    public DataResponse remittance(@RequestBody TransactionRequestDTO.Remittance remittance, @AuthenticationPrincipal String email){
        log.info(email);
        return bankAccountService.remittance(remittance, email);
    }
}
