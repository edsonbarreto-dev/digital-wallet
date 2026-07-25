package br.com.agendy.wallet.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class AccountTest {

  @Test
  void nova_conta_comeca_com_saldo_zero() {
    Account conta = Account.open();
    assertThat(conta.balance().amount()).isEqualByComparingTo(BigDecimal.ZERO);
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

    assertThat(conta.transactions()).hasSize(2);           // depósito + saque
    Transaction ultima = conta.transactions().get(1);
    assertThat(ultima.type()).isEqualTo(TransactionType.WITHDRAW);
    assertThat(ultima.amount()).isEqualTo(Money.of(new BigDecimal("40.00")));
  }
}
