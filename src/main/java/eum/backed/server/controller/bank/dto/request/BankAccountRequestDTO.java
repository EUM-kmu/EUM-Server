package eum.backed.server.controller.bank.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class BankAccountRequestDTO {
    @Getter
    @Setter
    public static class CheckNickName{
        @NotEmpty(message = "송금할 사람의 닉네임을 입력하세요")
        private String nickname;
    }
    @Getter
    @Setter
    public static class Remittance{
        @NotEmpty(message = "송금할 사람의 닉네임을 입력하세요")
        private String nickname;
        @Positive(message = "금액은 양수여야 합니다")
        private Long amount;
        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "\\d{4}", message = "비밀번호는 4자리 숫자여야 합니다.")
        private String password;
    }
    @Getter
    @Setter
    public static class UpdatePassword {
        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "\\d{4}", message = "비밀번호는 4자리 숫자여야 합니다.")
        private String currentPassword;
        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "\\d{4}", message = "비밀번호는 4자리 숫자여야 합니다.")
        private String newPassword;
    }
    @Getter
    @Setter
    public static class Password {
        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "\\d{4}", message = "비밀번호는 4자리 숫자여야 합니다.")
        private String password;
    }
    @Getter
    @Setter
    public static class CardName {
        private String cardName;
    }
}
