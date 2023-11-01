package eum.backed.server.domain.community.apply;

import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply,Long> {
    Optional<List<Apply>> findByTransactionPostOrderByCreateDateDesc(TransactionPost transactionPost);

    Boolean existsByUserAndTransactionPost(Users user, TransactionPost transactionPost);
}
