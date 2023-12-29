package eum.backed.server.domain.community.likeopinionpost;

import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeOpinionPostRepository extends JpaRepository<LikeOpinionPost,Long> {
    Boolean existsByUserAndOpinionPost(Users user, OpinionPost opinionPost);

    LikeOpinionPost findByUserAndOpinionPost(Users user, OpinionPost opinionPost);
}
