package br.com.digital.wallet.domain;

public record MoneyWithdrawn(Money amount) implements DomainEvent {
}