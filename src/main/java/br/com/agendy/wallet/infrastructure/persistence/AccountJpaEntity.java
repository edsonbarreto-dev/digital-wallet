package br.com.agendy.wallet.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "account")
class AccountJpaEntity {

  @Id
  private UUID id;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal balance;

  private Long version;

  protected AccountJpaEntity() {
  }

  AccountJpaEntity(UUID id, BigDecimal balance) {
    this(id, balance, null);
  }

  AccountJpaEntity(UUID id, BigDecimal balance, Long version) {
    this.id = id;
    this.balance = balance;
    this.version = version;
  }

  UUID getId() {
    return id;
  }

  BigDecimal getBalance() {
    return balance;
  }

  Long getVersion() {
    return version;
  }
}
