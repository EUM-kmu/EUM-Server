package eum.backed.server.domain.community.comment;

import eum.backed.server.domain.community.post.Post;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByPostOrderByCreateDateDesc(Post post);
    boolean existsByUser(Users users);
}
