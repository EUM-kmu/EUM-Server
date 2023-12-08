package eum.backed.server.domain.community.marketpost;

import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.community.category.MarketCategory;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketPostRepository extends JpaRepository<MarketPost,Long> {
    Optional<List<MarketPost>> findByMarketCategoryAndIsDeletedFalseOrderByCreateDateDesc(MarketCategory marketCategory);

    Optional<List<MarketPost>> findByMarketCategoryAndMarketTypeAndIsDeletedFalseOrderByCreateDateDesc(MarketCategory marketCategory, MarketType marketType);

    Optional<List<MarketPost>> findByMarketCategoryAndMarketTypeAndStatusAndIsDeletedFalseOrderByCreateDateDesc(MarketCategory marketCategory, MarketType marketType, Status status);

    Optional<List<MarketPost>> findByMarketCategoryAndStatusAndIsDeletedFalseOrderByCreateDateDesc(MarketCategory marketCategory, Status status);

    Optional<List<MarketPost>> findByUserAndIsDeletedFalseOrderByCreateDateDesc(Users user);

    Optional<List<MarketPost>> findByTitleContainingAndIsDeletedFalseOrderByCreateDateDesc(String title);

    Optional<List<MarketPost>> findAllByIsDeletedFalseOrderByCreateDateDesc(Pageable pageable);

    Optional<List<MarketPost>> findByStatusAndIsDeletedFalseOrderByCreateDateDesc(Status status);
}
