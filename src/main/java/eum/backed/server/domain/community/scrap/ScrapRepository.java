package eum.backed.server.domain.community.scrap;

import eum.backed.server.domain.community.post.Post;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByPostAndUser(Post post,Users users);
    Optional<Scrap> findByPostAndUser(Post post, Users user);
    Optional<List<Scrap>> findByUserOrderByCreateDateDesc(Users users);
}
