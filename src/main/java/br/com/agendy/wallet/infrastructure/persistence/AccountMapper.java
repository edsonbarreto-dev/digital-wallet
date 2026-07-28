package br.com.agendy.wallet.infrastructure.persistence;

import br.com.agendy.wallet.domain.Account;

final class AccountMapper {

  private AccountMapper() {
  }

  static AccountJpaEntity toEntity(Account account) {
    throw new UnsupportedOperationException();
  }

  static Account toDomain(AccountJpaEntity entity) {
    throw new UnsupportedOperationException();
  }
}
