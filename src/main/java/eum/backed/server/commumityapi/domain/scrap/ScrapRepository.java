package eum.backed.server.commumityapi.domain.scrap;

import eum.backed.server.commumityapi.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByUser(Users users);
}
