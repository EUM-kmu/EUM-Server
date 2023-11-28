package eum.backed.server.domain.community.marketpost;

import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.community.category.MarketCategory;
import eum.backed.server.domain.community.region.DONG.Township;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketPostRepository extends JpaRepository<MarketPost,Long> {
    Optional<List<MarketPost>> findByMarketCategoryAndTownshipOrderByCreateDateDesc(MarketCategory marketCategory, Township townShip);

    Optional<List<MarketPost>> findByMarketCategoryAndTownshipAndMarketTypeOrderByCreateDateDesc(MarketCategory marketCategory, Township townShip, MarketType marketType);

    Optional<List<MarketPost>> findByMarketCategoryAndTownshipAndMarketTypeAndStatusOrderByCreateDateDesc(MarketCategory marketCategory, Township townShip, MarketType marketType, Status status);

    Optional<List<MarketPost>> findByMarketCategoryAndTownshipAndStatusOrderByCreateDateDesc(MarketCategory marketCategory, Township townShip, Status status);

    Optional<List<MarketPost>> findByUserOrderByCreateDateDesc(Users user);

    Optional<List<MarketPost>> findByTownshipAndTitleContainingOrderByCreateDateDesc(Township townShip, String title);

    List<MarketPost> findAllByOrderByCreateDateDesc();
}
