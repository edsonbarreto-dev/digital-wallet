package br.com.agendy.wallet.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class AccountTest {

  @Test
  void nova_conta_comeca_com_saldo_zero() {
    Account conta = Account.open();
    assertThat(conta.balance().amount()).isEqualByComparingTo(BigDecimal.ZERO);
  }

  @Test
  void nova_conta_nasce_com_identidade() {
    assertThat(Account.open().id()).isNotNull();
  }

  @Test
  void contas_diferentes_tem_identidades_diferentes() {
    UUID id1 = Account.open().id();
    UUID id2 = Account.open().id();
    assertThat(id1).isNotEqualTo(id2);
  }

  @Test
  void deposito_aumenta_o_saldo() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("10.00")));
    assertThat(conta.balance()).isEqualTo(Money.of(new BigDecimal("10.00")));
  }

  @Test
  void deposito_de_valor_zero_deve_falhar() {
    Account conta = Account.open();
    assertThatIllegalArgumentException()
      .isThrownBy(() -> conta.deposit(Money.of(BigDecimal.ZERO)));
  }

  @Test
  void saque_diminui_o_saldo() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("100.00")));

    conta.withdraw(Money.of(new BigDecimal("30.00")));

    assertThat(conta.balance()).isEqualTo(Money.of(new BigDecimal("70.00")));
  }

  @Test
  void saque_maior_que_o_saldo_deve_falhar() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("50.00")));

    assertThatExceptionOfType(InsufficientFundsException.class)
      .isThrownBy(() -> conta.withdraw(Money.of(new BigDecimal("50.01"))));
  }

  @Test
  void saque_de_valor_zero_deve_falhar() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("100.00")));

    assertThatIllegalArgumentException()
      .isThrownBy(() -> conta.withdraw(Money.of(BigDecimal.ZERO)));
  }

  @Test
  void nova_conta_nao_tem_transacoes() {
    assertThat(Account.open().transactions()).isEmpty();
  }

  @Test
  void deposito_registra_uma_transacao() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("100.00")));

    assertThat(conta.transactions()).hasSize(1);
    Transaction t = conta.transactions().get(0);
    assertThat(t.type()).isEqualTo(TransactionType.DEPOSIT);
    assertThat(t.amount()).isEqualTo(Money.of(new BigDecimal("100.00")));
  }

  @Test
  void saque_registra_uma_transacao() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("100.00")));
    conta.withdraw(Money.of(new BigDecimal("40.00")));

    assertThat(conta.transactions()).hasSize(2);
    Transaction ultima = conta.transactions().get(1);
    assertThat(ultima.type()).isEqualTo(TransactionType.WITHDRAW);
    assertThat(ultima.amount()).isEqualTo(Money.of(new BigDecimal("40.00")));
  }

  @Test
  void operacao_rejeitada_nao_registra_transacao() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("50.00")));

    assertThatExceptionOfType(InsufficientFundsException.class)
      .isThrownBy(() -> conta.withdraw(Money.of(new BigDecimal("100.00"))));

    assertThat(conta.transactions()).hasSize(1);
  }

  @Test
  void historico_exposto_nao_pode_ser_alterado_por_fora() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("10.00")));

    List<Transaction> historico = conta.transactions();
    assertThatExceptionOfType(UnsupportedOperationException.class)
      .isThrownBy(() -> historico.add(new Transaction(TransactionType.DEPOSIT, Money.of(new BigDecimal("999.00")))));
  }

  @Test
  void deposito_publica_evento_MoneyDeposited() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("100.00")));

    assertThat(conta.domainEvents()).hasSize(1);
    assertThat(conta.domainEvents().get(0))
      .isInstanceOfSatisfying(MoneyDeposited.class,
        e -> assertThat(e.amount()).isEqualTo(Money.of(new BigDecimal("100.00"))));
  }

  @Test
  void saque_publica_evento_MoneyWithdrawn() {
    Account conta = Account.open();
    conta.deposit(Money.of(new BigDecimal("100.00")));
    conta.withdraw(Money.of(new BigDecimal("30.00")));

    assertThat(conta.domainEvents()).hasSize(2);
    assertThat(conta.domainEvents().get(1)).isInstanceOf(MoneyWithdrawn.class);
  }

  @Test
  void operacao_rejeitada_nao_publica_evento() {
    Account conta = Account.open();

    assertThatIllegalArgumentException()
      .isThrownBy(() -> conta.deposit(Money.of(BigDecimal.ZERO)));

    assertThat(conta.domainEvents()).isEmpty();
  }
}
