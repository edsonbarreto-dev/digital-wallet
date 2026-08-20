package br.com.digital.wallet.domain;

public record MoneyDeposited(Money amount) implements DomainEvent {
}