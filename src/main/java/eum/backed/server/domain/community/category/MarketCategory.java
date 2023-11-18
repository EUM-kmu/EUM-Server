package eum.backed.server.domain.community.category;

import eum.backed.server.domain.community.marketpost.MarketPost;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MarketCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column
    private String contents;

    @OneToMany(mappedBy = "marketCategory", orphanRemoval = true)
    private List<MarketPost> marketPosts = new ArrayList<>();
}
