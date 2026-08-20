package br.com.digital.wallet.domain;

public sealed interface DomainEvent
  permits MoneyDeposited, MoneyWithdrawn {
}
