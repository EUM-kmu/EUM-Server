package eum.backed.server.domain.community.opinionpost;

import eum.backed.server.domain.community.region.DONG.Dong;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OpinionPostRepository extends JpaRepository<OpinionPost,Long> {
    Optional<List<OpinionPost>> findByDongOrderByCreateDateDesc(Dong dong);
    Optional <List<OpinionPost>> findByLikeCountGreaterThanOrderByLikeCountDesc(int likeCount);

    Optional<List<OpinionPost>> findByUserOrderByCreateDate(Users users);

    Optional<List<OpinionPost>> findByDongAndTitleContainingOrderByCreateDateDesc(Dong dong, String title);


}
