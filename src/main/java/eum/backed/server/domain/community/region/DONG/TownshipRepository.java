package eum.backed.server.domain.community.region.DONG;

import eum.backed.server.domain.community.region.GU.Town;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TownshipRepository extends JpaRepository<Township, Long> {
    Optional<Township> findByName(String name);

    Optional<List<Township>> findByTown(Town town);
}
