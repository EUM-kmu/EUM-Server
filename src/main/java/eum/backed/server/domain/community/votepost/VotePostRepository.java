package eum.backed.server.domain.community.votepost;

import eum.backed.server.domain.community.region.Regions;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VotePostRepository extends JpaRepository<VotePost,Long> {
    @Query("SELECT v FROM VotePost v " +
            "WHERE v.regions = :regions " +
            "AND v.isDeleted = false " +
            "AND (COALESCE(:users, NULL) IS NULL OR v.user NOT IN :users) " +
            "ORDER BY v.createDate DESC")
    Optional<List<VotePost>> findByRegionsOrderByCreateDateDesc(
            @Param("regions") Regions regions,
            @Param("users") List<Users> users
    );

    Optional<List<VotePost>> findByUserAndIsDeletedFalseOrderByCreateDateDesc(Users users);

    @Query("SELECT v FROM VotePost v " +
            "WHERE v.regions = :regions " +
            "AND v.title LIKE %:title% " +
            "AND v.isDeleted = false " +
            "AND (COALESCE(:users, NULL) IS NULL OR v.user NOT IN :users) " +
            "ORDER BY v.createDate DESC")
    Optional<List<VotePost>> findByRegionsAndTitleContainingOrderByCreateDateDesc(
            @Param("regions") Regions regions,
            @Param("title") String title,
            @Param("users") List<Users> users);
}
