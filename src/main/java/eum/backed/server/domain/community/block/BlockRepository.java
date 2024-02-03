package eum.backed.server.domain.community.block;

import eum.backed.server.domain.auth.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block,Long> {
//    @Query("SELECT b.blocker FROM Block b WHERE b.blocker = :blocker")
//    Optional<List<Users>> findByBlocker(@Param("blocker") Users blocker);
    Optional<List<Block>> findByBlocker(Users blocker);
//    Optional<List<Block>> findByBlocked(Users blocker);
    Optional<List<Block>> findByBlocked( Users blocked);

    Boolean existsByBlockerAndBlocked(Users blocker, Users blocked);
    Optional<Block> findByBlockerAndBlocked(Users blocker, Users blocked);
}
