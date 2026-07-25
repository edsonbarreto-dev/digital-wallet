package br.com.agendy.wallet.domain;

public record MoneyDeposited(Money amount) implements DomainEvent {
}