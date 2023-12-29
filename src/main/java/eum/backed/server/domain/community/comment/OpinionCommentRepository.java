package eum.backed.server.domain.community.comment;

import eum.backed.server.domain.community.opinionpost.OpinionPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OpinionCommentRepository extends JpaRepository<OpinionComment, Long> {
    Optional<List<OpinionComment>> findByOpinionPostOrderByCreateDateDesc(OpinionPost opinionPost);

}

