package eum.backed.server.domain.community.promotionpost;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.category.PromotionCategory;
import eum.backed.server.domain.community.user.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PromotionPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionPostId;

    @Column
    private String title;
    private String content;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "promotion_category_id")
    private PromotionCategory promotionCategory;
}
