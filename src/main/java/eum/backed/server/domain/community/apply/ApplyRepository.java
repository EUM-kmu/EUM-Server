package eum.backed.server.domain.community.apply;

import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.auth.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply,Long> {
    Optional<List<Apply>> findByMarketPostOrderByCreateDateDesc(MarketPost marketPost);

    Boolean existsByUserAndMarketPost(Users user, MarketPost marketPost);
    Optional<Apply> findByUserAndMarketPost(Users user, MarketPost marketPost);

    Optional<List<Apply>> findByUser(Users user);
    @Modifying
    @Query("DELETE FROM Apply a WHERE a.user = :applicant AND a.marketPost IN :posts AND a.status = 'WAITING'")
    void deleteApplyByApplicantAndPostIn(@Param("applicant") Users applicant, @Param("posts") List<MarketPost> posts);
    @Query("SELECT a FROM Apply a WHERE a.user = :applicant AND a.marketPost IN :posts AND a.status = 'TRADING' OR a.status ='WAITING'")
    Optional<List<Apply>> findTradingAppliesByApplicantAndPostIn(@Param("applicant") Users applicant, @Param("posts") List<MarketPost> posts);

    @Query("SELECT a FROM Apply a WHERE a.user = :user AND a.marketPost.isDeleted = false ")
    Optional<List<Apply>> findByUserIsDeletedFalse(@Param("user") Users user);




}
