package eum.backed.server.domain.community.opinionpost;

import eum.backed.server.domain.community.region.Regions;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OpinionPostRepository extends JpaRepository<OpinionPost,Long> {
    Optional<List<OpinionPost>> findByUserAndIsDeletedFalseOrderByCreateDate(Users users);
    @Query("SELECT op FROM OpinionPost op " +
            "WHERE op.regions = :regions " +
            "AND op.isDeleted = false " +
            "AND (COALESCE(:users, NULL) IS NULL OR op.user NOT IN :users) " +
            "ORDER BY op.createDate DESC")
    Optional<List<OpinionPost>> findByRegionsAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(
            @Param("regions") Regions regions,
            @Param("users") List<Users> users
    );

    @Query("SELECT op FROM OpinionPost op " +
            "WHERE op.regions = :regions " +
            "AND op.likeCount > :likeCount " +
            "AND op.isDeleted = false " +
            "AND (COALESCE(:users, NULL) IS NULL OR op.user NOT IN :users) " +
            "ORDER BY op.createDate DESC")
    Optional<List<OpinionPost>> findByRegionsAndLikeCountGreaterThanAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(
            @Param("regions") Regions regions,
            @Param("likeCount") int likeCount,
            @Param("users") List<Users> users
    );

    @Query("SELECT op FROM OpinionPost op " +
            "WHERE op.regions = :regions " +
            "AND op.title LIKE %:title% " +
            "AND op.isDeleted = false " +
            "AND (COALESCE(:users, NULL) IS NULL OR op.user NOT IN :users) " +
            "ORDER BY op.createDate DESC")
    Optional<List<OpinionPost>> findByRegionsAndTitleContainingAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(
            @Param("regions") Regions regions,
            @Param("title") String title,
            @Param("users") List<Users> users
    );


}
