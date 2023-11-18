package eum.backed.server.domain.community.region.DONG;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TownshipRepository extends JpaRepository<Township, Long> {
    Optional<Township> findByName(String name);
}
