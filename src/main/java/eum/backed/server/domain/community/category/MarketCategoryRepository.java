package eum.backed.server.domain.community.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yaml.snakeyaml.error.Mark;

import java.util.Optional;

public interface MarketCategoryRepository extends JpaRepository<MarketCategory,Long> {
    Optional<MarketCategory> findByContents(String content);
}
