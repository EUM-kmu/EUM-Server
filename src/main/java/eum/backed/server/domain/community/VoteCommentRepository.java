package eum.backed.server.domain.community;

import eum.backed.server.domain.community.comment.VoteComment;
import eum.backed.server.domain.community.votepost.VotePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteCommentRepository extends JpaRepository<VoteComment,Long> {
    Optional<List<VoteComment>> findByVotePostOrderByCreateDateDesc(VotePost votePost);

}
