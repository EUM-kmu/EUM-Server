package eum.backed.server.domain.community.post;

import eum.backed.server.domain.community.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    Optional<List<Post>> findByCategoryOrderByCreateDateDesc(Category category);

    Optional<List<Post>> findByStatusOrderByCreateDateDesc(Status status);

    Optional<List<Post>> findByNeedHelperOrderByCreateDateDesc(Boolean needHelper);
}
