package eum.backed.server.domain.bank.userbankaccount;

import eum.backed.server.domain.auth.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBankAccountRepository extends JpaRepository<UserBankAccount, Long> {
    Optional<UserBankAccount> findByUser(Users users);
    Boolean existsByUser(Users users);

}
