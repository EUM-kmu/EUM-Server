package eum.backed.server.domain.community.withdrawaluser;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.auth.user.Users;
import eum.backed.server.domain.community.withdrawalcategory.WithdrawalCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WithdrawalUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawalUserId;

    @Column
    private String reason;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "withdrawal_category_id")
    private WithdrawalCategory withdrawalCategory;

    public static WithdrawalUser toEntity(Users user, String reason, WithdrawalCategory withdrawalCategory){
        return WithdrawalUser.builder()
                .user(user)
                .reason(reason)
                .withdrawalCategory(withdrawalCategory).build();
    }

}
