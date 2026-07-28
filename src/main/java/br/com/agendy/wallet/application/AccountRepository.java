package br.com.agendy.wallet.application;

import br.com.agendy.wallet.domain.Account;

import java.util.Optional;
import java.util.UUID;

/**
 * Porta de saída, definida na camada de APLICAÇÃO (Clean Architecture — Uncle Bob):
 * são os casos de uso que declaram o que precisam do mundo externo. O domínio (entidades
 * e VOs) fica no círculo mais interno, sem nenhuma interface "olhando pra fora".
 *
 * O contrato fala só a língua do domínio (Account, UUID) + Optional — nada de JPA, SQL ou
 * Spring. Quem cumpre é um adaptador na borda: Inversão de Dependência no limite da arquitetura.
 */
public interface AccountRepository {

  /** Persiste a conta (insere ou atualiza, conforme a identidade). Devolve o agregado salvo. */
  Account save(Account account);

  /** Busca uma conta pela identidade. Optional vazio quando não existe — nunca null. */
  Optional<Account> findById(UUID id);
}
