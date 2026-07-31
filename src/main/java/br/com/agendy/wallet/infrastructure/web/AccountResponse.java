package br.com.agendy.wallet.infrastructure.web;

import br.com.agendy.wallet.domain.Account;

/**
 * DTO de resposta da API. Não expõe o agregado direto: o dinheiro trafega como String
 * (ex.: "0.00") para o cliente não sofrer arredondamento de ponto flutuante ao ler valores.
 */
record AccountResponse(String id, String balance) {

  static AccountResponse from(Account account) {
    return new AccountResponse(account.id().toString(), account.balance().amount().toPlainString());
  }
}
