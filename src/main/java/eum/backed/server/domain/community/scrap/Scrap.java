package eum.backed.server.domain.community.scrap;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.auth.user.Users;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Scrap extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="transaction_post_id")
    private MarketPost marketPost;
}
