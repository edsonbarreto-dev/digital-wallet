package br.com.agendy.wallet.infrastructure.persistence;

import br.com.agendy.wallet.application.AccountRepository;
import br.com.agendy.wallet.domain.Account;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
class AccountPersistenceAdapter implements AccountRepository {

  private final AccountJpaRepository jpa;

  AccountPersistenceAdapter(AccountJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public Account save(Account account) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<Account> findById(UUID id) {
    throw new UnsupportedOperationException();
  }
}
