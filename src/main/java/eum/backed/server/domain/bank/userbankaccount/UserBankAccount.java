package eum.backed.server.domain.bank.userbankaccount;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.auth.user.Users;
import eum.backed.server.domain.bank.Owner;
import eum.backed.server.domain.bank.bankacounttransaction.BankAccountTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
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
                .isFreeze(false)
                .owner(Owner.USER)
                .balance(0L)
                .user(user)
                .build();
    }

}
