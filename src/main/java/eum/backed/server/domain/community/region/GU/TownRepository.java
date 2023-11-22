package eum.backed.server.domain.community.region.GU;

import eum.backed.server.domain.community.region.SI.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TownRepository extends JpaRepository<Town,Long> {
    Optional<List<Town>> findByCity(City city);

    Optional<Town> findByName(String name);
}
