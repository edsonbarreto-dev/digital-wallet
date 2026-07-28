package br.com.agendy.wallet.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Modelo de PERSISTÊNCIA (não é o domínio!). É um espelho da conta desenhado para o
 * banco: só dados + anotações JPA. O agregado Account continua puro; um mapper traduz
 * um no outro. Assim as restrições do ORM (construtor sem-args, campos mutáveis) ficam
 * confinadas aqui, na borda.
 */
@Entity
@Table(name = "account")
class AccountJpaEntity {

  @Id
  private UUID id;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal balance;

  /** Exigido pelo JPA: o Hibernate instancia via reflexão e depois preenche os campos. */
  protected AccountJpaEntity() {
  }

  AccountJpaEntity(UUID id, BigDecimal balance) {
    this.id = id;
    this.balance = balance;
  }

  UUID getId() {
    return id;
  }

  BigDecimal getBalance() {
    return balance;
  }
}
