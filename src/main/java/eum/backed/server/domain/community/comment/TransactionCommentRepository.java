package eum.backed.server.domain.community.comment;

import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionCommentRepository extends JpaRepository<TransactionComment, Long> {
    Optional<List<TransactionComment>> findByTransactionPostOrderByCreateDateDesc(TransactionPost transarctionPost);
    boolean existsByUser(Users users);
}
