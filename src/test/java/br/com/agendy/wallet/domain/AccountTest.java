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
}
