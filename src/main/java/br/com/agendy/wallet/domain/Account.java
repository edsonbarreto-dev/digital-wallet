package br.com.agendy.wallet.domain;

import java.math.BigDecimal;

public class Account {

  private Money balance;

  private Account() {
    this.balance = Money.of(BigDecimal.ZERO);
  }

  public static Account open() {
    return new Account();
  }

  public Money balance() {
    return balance;
  }

  public void deposit(Money money) {
    this.balance = this.balance.add(money);
  }
}
