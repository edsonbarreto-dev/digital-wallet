package br.com.digital.wallet.infrastructure.persistence;

import br.com.digital.wallet.application.AccountRepository;
import br.com.digital.wallet.domain.Account;
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
    AccountJpaEntity saved = jpa.save(AccountMapper.toEntity(account));
    return AccountMapper.toDomain(saved);
  }

  @Override
  public Optional<Account> findById(UUID id) {
    return jpa.findById(id).map(AccountMapper::toDomain);
  }
}
