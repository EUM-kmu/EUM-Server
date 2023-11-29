package eum.backed.server.domain.community.marketpost;

import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.community.category.MarketCategory;
import eum.backed.server.domain.community.region.Regions;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketPostRepository extends JpaRepository<MarketPost,Long> {
    Optional<List<MarketPost>> findByMarketCategoryAndRegionsOrderByCreateDateDesc(MarketCategory marketCategory, Regions regions);

    Optional<List<MarketPost>> findByMarketCategoryAndRegionsAndMarketTypeOrderByCreateDateDesc(MarketCategory marketCategory, Regions regions, MarketType marketType);

    Optional<List<MarketPost>> findByMarketCategoryAndRegionsAndMarketTypeAndStatusOrderByCreateDateDesc(MarketCategory marketCategory, Regions regions, MarketType marketType, Status status);

    Optional<List<MarketPost>> findByMarketCategoryAndRegionsAndStatusOrderByCreateDateDesc(MarketCategory marketCategory, Regions regions, Status status);

    Optional<List<MarketPost>> findByUserOrderByCreateDateDesc(Users user);

    Optional<List<MarketPost>> findByRegionsAndTitleContainingOrderByCreateDateDesc(Regions regions, String title);

    List<MarketPost> findAllByOrderByCreateDateDesc();
}
