package eum.backed.server.domain.community.apply;

import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply,Long> {
    Optional<List<Apply>> findByMarketPostOrderByCreateDateDesc(MarketPost marketPost);

    Boolean existsByUserAndMarketPost(Users user, MarketPost marketPost);
    Optional<Apply> findByUserAndMarketPost(Users user, MarketPost marketPost);

    Optional<List<Apply>> findByUser(Users user);

}
