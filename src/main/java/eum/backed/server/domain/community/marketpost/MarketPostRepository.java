package eum.backed.server.domain.community.marketpost;

import eum.backed.server.controller.community.DTO.request.enums.MarketType;
import eum.backed.server.domain.community.category.MarketCategory;
import eum.backed.server.domain.community.user.Users;
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
//            "AND (COALESCE(:users, NULL) IS NULL OR e.user NOT IN :users) " +
            "ORDER BY e.createDate DESC")
    Optional<List<MarketPost>> findByFilters(
            @Param("marketCategory") MarketCategory marketCategory,
            @Param("marketType") MarketType marketType,
            @Param("status") Status status
//            @Param("users") List<Users> users
    );


    Optional<List<MarketPost>> findByUserAndIsDeletedFalseOrderByCreateDateDesc(Users user);
    Optional<List<MarketPost>> findByUserAndIsDeletedFalse(Users user);

    @Query("SELECT mp FROM MarketPost mp " +
            "WHERE mp.title LIKE %:title% " +
            "AND mp.isDeleted = false " +
            "AND (COALESCE(:users, NULL) IS NULL OR mp.user NOT IN :users) " +
            "ORDER BY mp.createDate DESC")
    Optional<List<MarketPost>> findByKeywords(
            @Param("title") String title,
            @Param("users") List<Users> users
    );

}
