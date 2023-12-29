package eum.backed.server.domain.community.category;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.promotionpost.PromotionPost;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PromotionCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionCategoryId;

    @Column
    private String content;

    @OneToMany(mappedBy = "promotionCategory", orphanRemoval = true)
    private List<PromotionPost> promotionPosts = new ArrayList<>();
}
