package br.com.agendy.wallet.application;

import br.com.agendy.wallet.domain.Account;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Caso de uso: depositar em uma conta. Carrega o agregado pela porta, aplica a regra de
 * domínio (Account.deposit) e persiste. A invariante de valor positivo mora no domínio.
 */
public class DepositService {

  private final AccountRepository accounts;

  public DepositService(AccountRepository accounts) {
    this.accounts = accounts;
  }

  public Account deposit(UUID accountId, BigDecimal amount) {
    throw new UnsupportedOperationException("stub — TDD red do depósito");
  }
}
