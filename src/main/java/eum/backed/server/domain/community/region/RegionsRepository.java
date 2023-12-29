package eum.backed.server.domain.community.region;

import eum.backed.server.domain.community.region.RegionType;
import eum.backed.server.domain.community.region.Regions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionsRepository extends JpaRepository<Regions, Long> {
    Optional<List<Regions>> findByRegionType(RegionType regionType);

    Optional<List<Regions>> findByParent(Regions regions);
//    Optional<Regions> findByName(String name);

//    Optional<List<Regions>> findByrTown(Town town);
}
