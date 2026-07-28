package br.com.agendy.wallet.infrastructure.persistence;

import br.com.agendy.wallet.domain.Account;
import br.com.agendy.wallet.domain.Money;

final class AccountMapper {

  private AccountMapper() {
  }

  static AccountJpaEntity toEntity(Account account) {
    return new AccountJpaEntity(account.id(), account.balance().amount());
  }

  static Account toDomain(AccountJpaEntity entity) {
    return Account.restore(entity.getId(), Money.of(entity.getBalance()));
  }
}
