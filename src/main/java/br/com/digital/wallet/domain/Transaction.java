package br.com.digital.wallet.domain;

public record Transaction(TransactionType type, Money amount) {

  public Transaction {
    if (type == null || amount == null) {
      throw new IllegalArgumentException("Transação exige tipo e valor");
    }
  }
}