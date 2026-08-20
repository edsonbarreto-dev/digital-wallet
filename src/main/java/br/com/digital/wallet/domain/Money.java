package br.com.digital.wallet.domain;

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

  public Money add(Money other) {
    return new Money(amount.add(other.amount));
  }

  public Money subtract(Money other) {
    return new Money(amount.subtract(other.amount));
  }

  public boolean isZero() {
    return amount.signum() == 0;
  }

  public boolean isGreaterThan(Money other) {
    return this.amount.compareTo(other.amount) > 0;
  }
}
