package eum.backed.server.domain.bank.userbankaccount;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.enums.Owner;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

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
    private int balance;
    @Column
    @Enumerated(EnumType.STRING)
    private Owner owner;

    @OneToOne
    @JoinColumn(name="user_id")
    private Users user;

    public void withDraw(int balance) {
        this.balance -= balance;
    }
    public void deposit(int balance){
        this.balance += balance;
    }
    public static UserBankAccount toEntity(String nickname, String password, Users user){
        return UserBankAccount.builder()
                .accountName(nickname)
                .password(password)
                .owner(Owner.USER)
                .balance(0)
                .user(user)
                .build();
    }

}
