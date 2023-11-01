package eum.backed.server.domain.community.opinionpost;

import eum.backed.server.domain.community.region.DONG.Dong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OpinionPostRepository extends JpaRepository<OpinionPost,Long> {
    Optional<List<OpinionPost>> findByDongOrderByCreateDateDesc(Dong dong);
}
