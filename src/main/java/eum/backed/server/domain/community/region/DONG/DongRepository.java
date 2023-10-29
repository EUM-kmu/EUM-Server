package eum.backed.server.domain.community.region.DONG;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DongRepository extends JpaRepository<Dong, Long> {
    Optional<Dong> findByDong(String dong);
}
