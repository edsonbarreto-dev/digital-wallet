package br.com.digital.wallet.infrastructure.persistence;

import br.com.digital.wallet.domain.Account;
import br.com.digital.wallet.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class AccountMapperTest {

  @Test
  void converte_dominio_para_entidade() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("100.00")));

    AccountJpaEntity entity = AccountMapper.toEntity(conta);

    assertThat(entity.getId()).isEqualTo(conta.id());
    assertThat(entity.getBalance()).isEqualByComparingTo("100.00");
  }

  @Test
  void converte_entidade_para_dominio() {
    UUID id = UUID.randomUUID();
    AccountJpaEntity entity = new AccountJpaEntity(id, new BigDecimal("70.00"));

    Account conta = AccountMapper.toDomain(entity);

    assertThat(conta.id()).isEqualTo(id);
    assertThat(conta.balance()).isEqualTo(Money.of(new BigDecimal("70.00")));
  }

  @Test
  void ida_e_volta_preserva_id_e_saldo() {
    Account original = Account.open();
    original.deposit(Money.of(new BigDecimal("42.50")));

    Account resultado = AccountMapper.toDomain(AccountMapper.toEntity(original));

    assertThat(resultado.id()).isEqualTo(original.id());
    assertThat(resultado.balance()).isEqualTo(Money.of(new BigDecimal("42.50")));
  }

  @Test
  void carrega_a_versao_do_agregado_para_a_entidade() {
    Account conta = Account.restore(UUID.randomUUID(), Money.of(new BigDecimal("100.00")), 7L);

    AccountJpaEntity entity = AccountMapper.toEntity(conta);

    assertThat(entity.getVersion()).isEqualTo(7L);
  }

  @Test
  void carrega_a_versao_da_entidade_para_o_agregado() {
    AccountJpaEntity entity = new AccountJpaEntity(UUID.randomUUID(), new BigDecimal("70.00"), 5L);

    Account conta = AccountMapper.toDomain(entity);

    assertThat(conta.version()).isEqualTo(5L);
  }
}
