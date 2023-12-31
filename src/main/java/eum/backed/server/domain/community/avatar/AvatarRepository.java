package eum.backed.server.domain.community.avatar;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar,Long> {

    Optional<Avatar> findByAvatarNameAndStandard(AvatarName avatarName, Standard standard);
    Optional<Avatar> findByAvatarName(AvatarName avatarName);

}
