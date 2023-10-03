package eum.backed.server.commumityapi.domain.scrap;

import eum.backed.server.commumityapi.domain.post.Post;
import eum.backed.server.commumityapi.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByUser(Users users);
    Optional<Scrap> findByPostAndUser(Post post, Users user);
}
