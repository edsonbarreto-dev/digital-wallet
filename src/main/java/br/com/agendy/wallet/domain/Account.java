package br.com.agendy.wallet.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account {

  private Money balance;
  private final List<Transaction> transactions = new ArrayList<>();

  private Account() {
    this.balance = Money.of(BigDecimal.ZERO);
  }

  public static Account open() {
    return new Account();
  }

  public Money balance() {
    return balance;
  }

  public List<Transaction> transactions() {
    return List.copyOf(transactions);
  }

  public void deposit(Money amount) {
    if (amount.isZero()) {
      throw new IllegalArgumentException("Depósito deve ser maior que zero");
    }
    balance = balance.add(amount);
    this.transactions.add(new Transaction(TransactionType.DEPOSIT, amount));
  }

  public void withdraw(Money amount) {
    if (amount.isZero()) {
      throw new IllegalArgumentException("Saque deve ser maior que zero");
    }
    if (amount.isGreaterThan(this.balance)) {
      throw new InsufficientFundsException("Saldo insuficiente para saque");
    }
    this.balance = this.balance.subtract(amount);
    this.transactions.add(new Transaction(TransactionType.WITHDRAW, amount));
  }
}
