package eum.backed.server.domain.bank.bankacounttransaction;

import eum.backed.server.service.bank.BankAccountService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountTransactionRepository extends JpaRepository<BankAccountTransaction,Long> {
}
