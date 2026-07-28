package br.com.agendy.wallet.infrastructure.persistence;

import br.com.agendy.wallet.domain.Account;
import br.com.agendy.wallet.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
  "spring.datasource.url=jdbc:h2:mem:walletdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
  "spring.datasource.username=sa",
  "spring.datasource.password=",
  "spring.datasource.driver-class-name=org.h2.Driver",
  "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AccountPersistenceAdapterTest {

  @Autowired
  private AccountPersistenceAdapter adapter;

  @Test
  void salva_e_recupera_a_conta() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("100.00")));

    adapter.save(conta);

    Account recuperada = adapter.findById(conta.id()).orElseThrow();
    assertThat(recuperada.id()).isEqualTo(conta.id());
    assertThat(recuperada.balance()).isEqualTo(Money.of(new BigDecimal("100.00")));
  }

  @Test
  void conta_inexistente_retorna_vazio() {
    assertThat(adapter.findById(UUID.randomUUID())).isEmpty();
  }
}
