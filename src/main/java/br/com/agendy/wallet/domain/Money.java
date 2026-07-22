package br.com.agendy.wallet.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {

  public Money {
    if (amount.signum() < 0) {
      throw new IllegalArgumentException("O valor não pode ser negativo");
    }
    amount = amount.setScale(2, RoundingMode.HALF_EVEN);
  }

  // fábrica
  public static Money of(BigDecimal amount) {
    return new Money(amount);
  }

  // soma
  public Money add(Money other) {
    return new Money(amount.add(other.amount));
  }
}
