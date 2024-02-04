package eum.backed.server.domain.community.avatar;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar,Long> {

    Optional<Avatar> findByAvatarId(Long avatarId);

    List<Avatar> findAllByAvatarIdNot(Long avatarId);

}
