package eum.backed.server.controller.bank.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class TransactionRequestDTO {
    @Getter
    @Setter
    public static class Remittance{
        @NotEmpty
        private String receiverNickname;
        @NotEmpty
        private int amount;
    }
}
