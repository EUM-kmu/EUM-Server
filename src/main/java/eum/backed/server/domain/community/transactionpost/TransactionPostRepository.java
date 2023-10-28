package eum.backed.server.domain.community.transactionpost;

import eum.backed.server.domain.community.category.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionPostRepository extends JpaRepository<TransactionPost,Long> {
    Optional<List<TransactionPost>> findByTransactionCategoryOrderByCreateDateDesc(TransactionCategory transactionCategory);

    Optional<List<TransactionPost>> findByStatusOrderByCreateDateDesc(Status status);

    Optional<List<TransactionPost>> findByNeedHelperOrderByCreateDateDesc(Boolean needHelper);
}
