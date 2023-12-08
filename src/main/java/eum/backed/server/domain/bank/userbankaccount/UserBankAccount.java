package eum.backed.server.domain.bank.userbankaccount;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransaction;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.enums.Owner;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserBankAccount extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userBankAccountId;

    @Column
    private String accountName;
    private String password;
    private Long balance;
    private Boolean isFreeze;

    public void updateFreeze(Boolean freeze) {
        isFreeze = freeze;
    }

    @Column
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "MyEnum 값", allowableValues = "USER, ADMIN, MASTER")
    private Owner owner;

    @OneToOne
    @JoinColumn(name="user_id")
    private Users user;

    @OneToMany(mappedBy = "myBankAccount")
    private List<BankAccountTransaction> bankAccountTransactions = new ArrayList<>();
    public void withDraw(Long balance) {
        this.balance -= balance;
    }
    public void deposit(Long balance){
        this.balance += balance;
    }

    public void updateCardName(String accountName) {
        this.accountName = accountName;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public static UserBankAccount toEntity(String nickname, String password, Users user){
        return UserBankAccount.builder()
                .accountName(nickname)
                .password(password)
                .owner(Owner.USER)
                .balance(0L)
                .user(user)
                .build();
    }

}
