package eum.backed.server.domain.community.marketpost;

import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.domain.community.category.MarketCategory;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MarketPostRepository extends JpaRepository<MarketPost,Long> {

    @Query("SELECT e FROM MarketPost e " +
            "WHERE (:marketCategory IS NULL OR e.marketCategory = :marketCategory) " +
            "AND (:marketType IS NULL OR e.marketType = :marketType) " +
            "AND (:status IS NULL OR e.status = :status) " +
            "AND e.isDeleted = false " +
            "AND (COALESCE(:users, NULL) IS NULL OR e.user NOT IN :users) " +
            "ORDER BY e.createDate DESC")
    Optional<List<MarketPost>> findByFilters(
            @Param("marketCategory") MarketCategory marketCategory,
            @Param("marketType") MarketType marketType,
            @Param("status") Status status,
            @Param("users") List<Users> users
    );

    Optional<List<MarketPost>> findByMarketCategoryAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(MarketCategory marketCategory,List<Users> users);


    Optional<List<MarketPost>> findByMarketCategoryAndMarketTypeAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(MarketCategory marketCategory, MarketType marketType,List<Users> users);

    Optional<List<MarketPost>> findByMarketCategoryAndMarketTypeAndStatusAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(MarketCategory marketCategory, MarketType marketType, Status status,List<Users> users);

    Optional<List<MarketPost>> findByMarketCategoryAndStatusAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(MarketCategory marketCategory, Status status,List<Users> user);

    Optional<List<MarketPost>> findByUserAndIsDeletedFalseOrderByCreateDateDesc(Users user);
    Optional<List<MarketPost>> findByUserAndIsDeletedFalse(Users user);

    Optional<List<MarketPost>> findByTitleContainingAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(String title,List<Users>user);

    Optional<List<MarketPost>> findAllByIsDeletedFalseOrderByCreateDateDesc(Pageable pageable);

    Optional<List<MarketPost>> findByIsDeletedFalseAndUserNotInOrderByCreateDateDesc(List<Users> users);

    Optional<List<MarketPost>> findByStatusAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(Status status,List<Users> users);
}
