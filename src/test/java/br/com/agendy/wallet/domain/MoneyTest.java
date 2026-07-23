package br.com.agendy.wallet.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class MoneyTest {

  @Test
  void cria_money_com_valor() {
    Money dez = Money.of(new BigDecimal("10.00"));
  }

  @Test
  void nao_permite_valor_negativo() {
    assertThatThrownBy(() -> Money.of(new BigDecimal("-1")))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void dois_money_de_mesmo_valor_sao_iguais() {
    assertThat(Money.of(new BigDecimal("10.00")))
      .isEqualTo(Money.of(new BigDecimal("10.00")));
  }

  @Test
  void soma_dois_valores() {
    Money r = Money.of(new BigDecimal("10.00")).add(Money.of(new BigDecimal("5.50")));
      assertThat(r.amount()).isEqualByComparingTo("15.50");
  }

  @Test
  void subtrai_dois_valores() {
    Money r = Money.of(new BigDecimal("10.00")).subtract(Money.of(new BigDecimal("4.00")));
    assertThat(r.amount()).isEqualByComparingTo("6.00");
  }

  @Test
  void subtrai_alem_do_valor_viola_a_invariante() {
    assertThatThrownBy(() -> Money.of(new BigDecimal("10.00")).subtract(Money.of(new BigDecimal("15.00"))))
      .isInstanceOf(IllegalArgumentException.class);
  }
}
