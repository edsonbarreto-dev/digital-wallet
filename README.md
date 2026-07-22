# Digital Wallet

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen)
![Architecture](https://img.shields.io/badge/architecture-Clean-blueviolet)
![Tests](https://img.shields.io/badge/tests-TDD-success)
![License](https://img.shields.io/badge/license-MIT-blue)

A small **fintech domain** (a digital wallet) built the way real software should be built:
starting from the business problem, driven by tests, with the domain at the center.

> Study & portfolio project. The goal is not the framework — it's the **end-to-end process**:
> from a business demand to invariants, to failing tests, to clean code.

---

## What this project demonstrates

- **DDD (tactical patterns):** `Money` as an immutable **Value Object**, `Account` as an
  **Aggregate Root** that protects its **invariants** (no negative balance, funds check).
- **TDD (test-first):** every rule becomes a failing test before the code. The Git history
  tells the story — `test: … (red)` followed by `feat: … (green)`.
- **Clean Architecture:** the **Dependency Rule** — the domain knows nothing about Spring,
  JPA or HTTP. Frameworks are plugged in at the edges.
- **Traceability:** each test traces back to a documented business rule
  (see [`docs/`](docs/01-analise-de-dominio.md)).
- **Concurrency done right** (later releases): optimistic locking (`@Version`) and idempotency.

## Domain — Ubiquitous Language

| Term | Meaning |
|---|---|
| **Account** | Aggregate Root that holds the balance and protects its invariants |
| **Money** | Immutable Value Object (amount + currency); never negative |
| **Deposit / Withdraw** | Withdraw requires sufficient funds |
| **Transfer** | Atomic transfer between two accounts (all-or-nothing) |
| **Transaction** | Record of every movement |
| **Idempotency** | The same operation is never applied twice |

## Architecture (Clean Architecture)

Dependencies always point **inward**. The domain is pure Java.

```
adapter  ──►  application  ──►  domain      (domain depends on nothing)
   ▲
infrastructure  ── wires everything (Spring config) ──┘
```

```
br.com.agendy.wallet
├── domain/          Entities & Value Objects (Money, Account), invariants, domain events
├── application/     Use cases + ports (repository interfaces)
├── adapter/
│   ├── in/web/      REST controllers, DTOs
│   └── out/persistence/   JPA entities, repository implementation, mappers
└── infrastructure/  Spring configuration / wiring
```

## Design process

This repo documents **how we got to the code**, not just the code:
**Demand → Requirements → Domain discovery (DDD) → Invariants → Tests → Code.**
See [`docs/01-analise-de-dominio.md`](docs/01-analise-de-dominio.md) for the full chain,
including the traceability table that maps each test to the business rule that originated it.

## Getting started

**Prerequisites:** Java 21, Maven, Docker (for Postgres in later releases).

```bash
# Domain tests (pure, fast, no Docker needed)
mvn test

# Start Postgres for the running app (later releases)
docker compose up -d
mvn spring-boot:run
```

## Tech stack

Java 21 · Spring Boot 3 · Spring Data JPA · PostgreSQL · JUnit 5 · AssertJ ·
Testcontainers (from R2) · Flyway

## Roadmap

Built in small, tested releases:

- [x] **R0** — Scaffold (Spring Boot 3, Clean Architecture structure)
- [ ] **R1** — Domain core with TDD (`Money` ✅, `Account`, invariants, events)
- [ ] **R2** — Persistence (ports & adapters, optimistic locking, Testcontainers)
- [ ] **R3** — Use cases (deposit, withdraw, transfer, idempotency)
- [ ] **R4** — REST API (correct status codes, `ProblemDetail`, DTOs)
- [ ] **R5** — Concurrency & robustness (proven with tests)
- [ ] **R6** — Portfolio polish (diagrams, coverage, CI)

## License

[MIT](LICENSE) © Edson Silva Barreto
