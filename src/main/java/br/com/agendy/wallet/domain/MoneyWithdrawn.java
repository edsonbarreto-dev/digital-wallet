package br.com.agendy.wallet.domain;

public record MoneyWithdrawn(Money amount) implements DomainEvent {
}