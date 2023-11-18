package eum.backed.server.domain.community.scrap;

import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByMarketPostAndUser(MarketPost marketPost, Users users);
    Scrap findByMarketPostAndUser(MarketPost marketPost, Users user);
    Optional<List<Scrap>> findByUserOrderByCreateDateDesc(Users users);
}
