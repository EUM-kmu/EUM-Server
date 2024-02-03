package eum.backed.server.domain.community.comment;

import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.auth.user.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketCommentRepository extends JpaRepository<MarketComment, Long> {
    Optional<List<MarketComment>> findByMarketPostOrderByCreateDateDesc(MarketPost marketPost, Pageable pageable);
    boolean existsByUser(Users users);
}
