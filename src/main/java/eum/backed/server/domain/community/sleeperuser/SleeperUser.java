package eum.backed.server.domain.community.sleeperuser;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.user.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SleeperUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sleeperUserId;

    @Column
    private String reason;

    @ManyToOne
    @JoinColumn(name="user_id")
    Users user;
}
