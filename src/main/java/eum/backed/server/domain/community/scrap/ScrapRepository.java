package eum.backed.server.domain.community.scrap;

import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.auth.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByMarketPostAndUser(MarketPost marketPost, Users users);
    Scrap findByMarketPostAndUser(MarketPost marketPost, Users user);
    Optional<List<Scrap>> findByUserOrderByCreateDateDesc(Users users);

    @Query("SELECT s FROM Scrap s " +
            "WHERE s.user = :user " +
            "AND NOT EXISTS (SELECT 1 FROM s.marketPost.user bu WHERE bu IN :blockedUsers) " +
            "ORDER BY s.createDate DESC")
    Optional<List<Scrap>> findScrapPostsForUser(
            @Param("user") Users user,
            @Param("blockedUsers") List<Users> blockedUsers
    );


    Optional<List<Scrap>> findByMarketPost(MarketPost marketPost);
}
