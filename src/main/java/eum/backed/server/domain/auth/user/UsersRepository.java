package eum.backed.server.domain.auth.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByUidAndRole(String uid, Role role);

    Boolean existsByRole(Role role);
    Optional<Users> findByEmail(String username);
    Optional<Users> findByUid(String uid);

    Boolean existsByUid(String uid);


}
