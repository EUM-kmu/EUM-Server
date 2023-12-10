package eum.backed.server.domain.community.bank.userbankaccount;

import eum.backed.server.domain.community.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBankAccountRepository extends JpaRepository<UserBankAccount, Long> {
    Optional<UserBankAccount> findByUser(Users users);
    Boolean existsByUser(Users users);

}
