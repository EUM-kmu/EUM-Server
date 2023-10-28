package eum.backed.server.domain.community.scrap;

import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByTransactionPostAndUser(TransactionPost transactionPost, Users users);
    Optional<Scrap> findByTransactionPostAndUser(TransactionPost transactionPost, Users user);
    Optional<List<Scrap>> findByUserOrderByCreateDateDesc(Users users);
}
