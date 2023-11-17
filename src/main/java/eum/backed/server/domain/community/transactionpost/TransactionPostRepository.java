package eum.backed.server.domain.community.transactionpost;

import eum.backed.server.domain.community.category.TransactionCategory;
import eum.backed.server.domain.community.region.DONG.Dong;
import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionPostRepository extends JpaRepository<TransactionPost,Long> {
    Optional<List<TransactionPost>> findByTransactionCategoryOrderByCreateDateDesc(TransactionCategory transactionCategory);

    Optional<List<TransactionPost>> findByStatusOrderByCreateDateDesc(Status status);

    Optional<List<TransactionPost>> findByProvidingHelpOrderByCreateDateDesc(Boolean needHelper);

    Optional<List<TransactionPost>> findByUserOrderByCreateDateDesc(Users user);

    Optional<List<TransactionPost>> findByDongAndTitleContainingOrderByCreateDateDesc(Dong dong, String title);
}
