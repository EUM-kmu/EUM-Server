package eum.backed.server.domain.community.profile;

import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Boolean existsByNickname(String nickname);

    Optional<Profile> findByNickname(String nickName);

    Optional<Profile> findByUser(Users users);

}
