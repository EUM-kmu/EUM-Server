package eum.backed.server.commumityapi.domain.post;

import eum.backed.server.commumityapi.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    Optional<List<Post>> findByCategoryOrderByCreateDateDesc(Category category);

    Optional<List<Post>> findByStatusOrderByCreateDateDesc(Status status);

    Optional<List<Post>> findByNeedHelperOrderByCreateDateDesc(Boolean needHelper);
}
