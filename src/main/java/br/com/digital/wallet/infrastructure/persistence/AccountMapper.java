package br.com.digital.wallet.infrastructure.persistence;

import br.com.digital.wallet.domain.Account;
import br.com.digital.wallet.domain.Money;

final class AccountMapper {

  private AccountMapper() {
  }

  static AccountJpaEntity toEntity(Account account) {
    return new AccountJpaEntity(account.id(), account.balance().amount(), account.version());
  }

  static Account toDomain(AccountJpaEntity entity) {
    return Account.restore(entity.getId(), Money.of(entity.getBalance()), entity.getVersion());
  }
}
