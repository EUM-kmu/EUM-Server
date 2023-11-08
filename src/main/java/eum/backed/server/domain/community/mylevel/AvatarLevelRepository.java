package eum.backed.server.domain.community.mylevel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarLevelRepository extends JpaRepository<AvatarLevel,Long> {
    Optional<AvatarLevel> findByLevelName(String levelName);

}
