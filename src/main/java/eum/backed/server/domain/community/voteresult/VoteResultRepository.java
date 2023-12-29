package eum.backed.server.domain.community.voteresult;

import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.votepost.VotePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteResultRepository extends JpaRepository<VoteResult,Long> {
    Boolean existsByUserAndVotePost(Users user, VotePost votePost);
}
