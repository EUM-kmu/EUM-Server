package eum.backed.server.domain.bank.bankacounttransaction;

import eum.backed.server.domain.bank.userbankaccount.UserBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountTransactionRepository extends JpaRepository<BankAccountTransaction,Long> {
    Optional<List<BankAccountTransaction>> findByMyBankAccountOrderByCreateDateDesc(UserBankAccount myBankAccount);
    Optional<List<BankAccountTransaction>> findByMyBankAccountAndTrasnactionTypeOrderByCreateDateDesc(UserBankAccount myBankAccount,TrasnactionType trasnactionType);

}
