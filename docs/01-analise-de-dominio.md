# Análise de Domínio — Digital Wallet

> Este documento registra **como chegamos aos testes e ao código**. Ele vem ANTES da
> primeira linha de teste. A ordem é: **Demanda → Requisitos → Descoberta do domínio (DDD)
> → Invariantes → Testes → Código.** Nenhum teste nasce solto: cada um traduz uma regra.

---

## 1. A demanda (o problema de negócio)

> "Precisamos de uma **carteira digital** onde um titular possa **guardar dinheiro** e
> movimentá-lo — **depositar, sacar e transferir** — de forma **segura** e **sem
> inconsistências financeiras**. O sistema nunca pode permitir saldo negativo, dinheiro
> 'sumindo' ou uma mesma operação sendo aplicada duas vezes."

Essa é a fala do "cliente" (poderia ser um PO, uma área de produto ou o próprio negócio).
É daqui que tudo deriva.

---

## 2. Requisitos — histórias de usuário e critérios de aceite

**H1 — Abrir conta**
Como titular, quero abrir uma conta para guardar meu saldo.
- Dado que sou um novo titular, quando abro uma conta, então ela começa com **saldo zero**.

**H2 — Depositar**
Como titular, quero depositar dinheiro para aumentar meu saldo.
- Dado uma conta, quando deposito um valor **positivo**, então o saldo **aumenta** nesse valor.
- Depósito de valor **zero ou negativo** é **rejeitado**.

**H3 — Sacar**
Como titular, quero sacar, desde que tenha saldo suficiente.
- Dado saldo suficiente, quando saco, então o saldo **diminui**.
- Dado saldo **insuficiente**, o saque é **rejeitado** (`InsufficientFundsException`).

**H4 — Transferir**
Como titular, quero transferir para outra conta.
- A transferência é **atômica**: debita a origem e credita o destino, **tudo-ou-nada**.

**H5 — Não duplicar operação (idempotência)**
- Uma operação enviada duas vezes (mesma chave) é processada **uma só vez**.

---

## 3. Descoberta do domínio (DDD)

### 3.1 Linguagem ubíqua
Account (Conta), Titular, Saldo, **Money** (Dinheiro), Depósito, Saque, Transferência,
Transação, Fundos Insuficientes, Idempotência.

### 3.2 Mini event storming (comandos → eventos)
| Comando | Evento de domínio |
|---|---|
| Abrir conta | `AccountOpened` |
| Depositar | `MoneyDeposited` |
| Sacar | `MoneyWithdrawn` |
| Transferir | `TransferCompleted` |

### 3.3 Decisões de modelagem
- **`Money` é um Value Object**: representa uma *quantia*, **não tem identidade**, é
  **imutável** e é **comparado por valor** (R$10,00 é igual a R$10,00, sempre). Escolha
  clássica de DDD para modelar dinheiro — evita o erro de tratar valor monetário como
  `double`/`BigDecimal` solto espalhado pelo código.
- **`Account` é o Aggregate Root**: é a fronteira de consistência. Ninguém mexe no saldo
  "por fora"; toda alteração passa por métodos da `Account`, que **protegem as invariantes**.
- **`Transaction`** é registrada a cada movimentação (histórico/extrato).

### 3.4 Invariantes (regras que NUNCA podem ser violadas)
1. **Dinheiro não pode ser negativo.** (uma quantia é sempre ≥ 0)
2. **Operações em dinheiro só entre a mesma moeda.**
3. **Saldo nunca fica negativo** — saque só com fundos suficientes.
4. **Uma operação idempotente não é aplicada duas vezes.**

> As invariantes são o coração. É o que o `domain` protege, e é o que vira teste primeiro.

---

## 4. Rastreabilidade — da regra ao teste

É AQUI que os testes do `Money` ganham sentido. Cada um traduz uma decisão/invariante:

| Teste (`MoneyTest`) | De onde veio |
|---|---|
| `cria_money_com_valor` | Operação básica: preciso **construir** uma quantia (base de H2/H3/H4) |
| `nao_permite_valor_negativo` | **Invariante 1**: dinheiro não pode ser negativo |
| `dois_money_de_mesmo_valor_sao_iguais` | **Decisão 3.3**: `Money` é Value Object → igualdade por **valor** |
| `soma_dois_valores` | **H2/H4**: depositar e transferir exigem **somar** quantias |

Ou seja: o teste `nao_permite_valor_negativo` **não existe porque eu quis testar algo** —
ele existe porque o negócio disse "não pode saldo/dinheiro negativo". O teste é a
**especificação executável** dessa regra.

---

## 5. Por que TDD aqui

Escrever o teste primeiro força a gente a **transformar a regra de negócio em uma
verificação concreta** antes de codar. O teste vira:
1. **Especificação** — descreve o comportamento esperado, em código.
2. **Rede de segurança** — se um refactor quebrar a regra, o teste acende vermelho.
3. **Documentação viva** — quem ler os testes entende as regras sem ler o código todo.

Fluxo por invariante: **🔴 escrevo o teste da regra → 🟢 implemento o mínimo → 🔵 refatoro.**

---

## Próximos artefatos (à medida que avançarmos)
- `02-modelo-do-agregado.md` — diagrama do agregado `Account` (Mermaid).
- ADRs (Architecture Decision Records) — registrar decisões como "por que Clean Architecture",
  "por que lock otimista", etc.
