package eum.backed.server.commumityapi.domain.sleeperuser;

import eum.backed.server.commumityapi.domain.BaseTimeEntity;
import eum.backed.server.commumityapi.domain.user.Users;
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
