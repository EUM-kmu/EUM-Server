package eum.backed.server.domain.community.category;

import eum.backed.server.domain.community.marketpost.MarketPost;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
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
