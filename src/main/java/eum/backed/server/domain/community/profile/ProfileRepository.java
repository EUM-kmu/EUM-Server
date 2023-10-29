package eum.backed.server.domain.community.profile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Boolean existsByNickname(String nickname);
}
