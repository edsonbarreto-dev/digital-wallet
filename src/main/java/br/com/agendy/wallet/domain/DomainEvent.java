package br.com.agendy.wallet.domain;

public sealed interface DomainEvent
  permits MoneyDeposited, MoneyWithdrawn {
}
