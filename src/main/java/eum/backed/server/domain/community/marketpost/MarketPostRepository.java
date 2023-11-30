package eum.backed.server.domain.community.marketpost;

import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.community.category.MarketCategory;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketPostRepository extends JpaRepository<MarketPost,Long> {
    Optional<List<MarketPost>> findByMarketCategoryOrderByCreateDateDesc(MarketCategory marketCategory);

    Optional<List<MarketPost>> findByMarketCategoryAndMarketTypeOrderByCreateDateDesc(MarketCategory marketCategory, MarketType marketType);

    Optional<List<MarketPost>> findByMarketCategoryAndMarketTypeAndStatusOrderByCreateDateDesc(MarketCategory marketCategory, MarketType marketType, Status status);

    Optional<List<MarketPost>> findByMarketCategoryAndStatusOrderByCreateDateDesc(MarketCategory marketCategory, Status status);

    Optional<List<MarketPost>> findByUserOrderByCreateDateDesc(Users user);

    Optional<List<MarketPost>> findByTitleContainingOrderByCreateDateDesc(String title);

    List<MarketPost> findAllByOrderByCreateDateDesc(Pageable pageable);
}
