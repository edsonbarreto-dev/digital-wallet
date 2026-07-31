package br.com.agendy.wallet.application;

import br.com.agendy.wallet.domain.Account;

/**
 * Caso de uso: abrir uma nova conta.
 *
 * Fica na camada de APLICAÇÃO e orquestra o domínio (Account.open) + a porta de saída
 * (AccountRepository). Sem framework: quem constrói o bean é a infraestrutura.
 */
public class OpenAccountService {

  private final AccountRepository accounts;

  public OpenAccountService(AccountRepository accounts) {
    this.accounts = accounts;
  }

  public Account open() {
    throw new UnsupportedOperationException("stub — TDD red do R4");
  }
}
