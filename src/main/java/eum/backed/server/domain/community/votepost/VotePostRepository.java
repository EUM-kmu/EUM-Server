package eum.backed.server.domain.community.votepost;

import eum.backed.server.domain.community.region.DONG.Township;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VotePostRepository extends JpaRepository<VotePost,Long> {
    Optional<List<VotePost>> findByTownshipOrderByCreateDate(Township townShip);

    Optional<List<VotePost>> findByUserOrderByCreateDateDesc(Users users);

    Optional<List<VotePost>> findByTownshipAndTitleContainingOrderByCreateDateDesc(Township townShip, String title);
}
