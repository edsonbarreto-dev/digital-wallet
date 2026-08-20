package br.com.digital.wallet.infrastructure.idempotency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Especificação executável da idempotência: a mesma chave aplica a operação uma única vez
 * e devolve o resultado anterior; chaves diferentes executam cada uma. Sem Spring, rápido.
 */
class InMemoryIdempotencyRegistryTest {

  private final InMemoryIdempotencyRegistry registry = new InMemoryIdempotencyRegistry();

  @Test
  void mesma_chave_executa_uma_unica_vez_e_devolve_o_resultado_anterior() {
    AtomicInteger execucoes = new AtomicInteger(0);

    String primeira = registry.executeOnce("chave-1", () -> "resultado-" + execucoes.incrementAndGet());
    String reentrega = registry.executeOnce("chave-1", () -> "resultado-" + execucoes.incrementAndGet());

    assertThat(execucoes.get()).isEqualTo(1);      // a operação rodou UMA vez só
    assertThat(primeira).isEqualTo("resultado-1");
    assertThat(reentrega).isEqualTo("resultado-1"); // a reentrega recebe o resultado anterior
  }

  @Test
  void chaves_diferentes_executam_cada_uma() {
    AtomicInteger execucoes = new AtomicInteger(0);

    registry.executeOnce("chave-a", execucoes::incrementAndGet);
    registry.executeOnce("chave-b", execucoes::incrementAndGet);

    assertThat(execucoes.get()).isEqualTo(2);
  }
}
