package eum.backed.server.domain.community.sleeperuser;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.withdrawalcategory.WithdrawalCategory;
import lombok.*;
import org.hibernate.mapping.ToOne;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SleeperUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sleeperUserId;

    @OneToOne
    @JoinColumn(name="user_id")
    private Users user;


}
