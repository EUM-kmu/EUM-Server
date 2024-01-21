package eum.backed.server.domain.community.withdrawaluser;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.withdrawalcategory.WithdrawalCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
