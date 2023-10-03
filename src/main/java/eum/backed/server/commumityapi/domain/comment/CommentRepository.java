package eum.backed.server.commumityapi.domain.comment;

import eum.backed.server.commumityapi.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByPostOrderByCreateDateDesc(Post post);
}
