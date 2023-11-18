package eum.backed.server.controller.bank.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class BankAccountRequestDTO {
    @Getter
    @Setter
    public static class Remittance{
        @NotEmpty
        private String receiverNickname;
        @NotEmpty
        private Long amount;
        @NotEmpty
        private String password;
    }
    @Getter
    @Setter
    public static class UpdatePassword {
        private String currentPassword;
        private String newPassword;
    }
    @Getter
    @Setter
    public static class Password {
        private String password;
    }
}
