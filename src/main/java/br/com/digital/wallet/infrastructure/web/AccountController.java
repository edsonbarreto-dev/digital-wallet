package br.com.digital.wallet.infrastructure.web;

import br.com.digital.wallet.application.DepositService;
import br.com.digital.wallet.application.IdempotencyRegistry;
import br.com.digital.wallet.application.OpenAccountService;
import br.com.digital.wallet.domain.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
class AccountController {

  private final OpenAccountService openAccount;
  private final DepositService deposit;
  private final IdempotencyRegistry idempotency;

  AccountController(OpenAccountService openAccount, DepositService deposit,
                    IdempotencyRegistry idempotency) {
    this.openAccount = openAccount;
    this.deposit = deposit;
    this.idempotency = idempotency;
  }

  @PostMapping
  ResponseEntity<AccountResponse> open() {
    Account account = openAccount.open();
    return ResponseEntity
      .created(URI.create("/accounts/" + account.id()))
      .body(AccountResponse.from(account));
  }

  @PostMapping("/{id}/deposits")
  ResponseEntity<AccountResponse> deposit(
      @PathVariable UUID id,
      @RequestBody DepositRequest request,
      @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

    // Idempotência da requisição: a chave vem do header (concern de borda). Com a mesma
    // chave, o depósito é aplicado UMA vez — uma reentrega devolve o resultado anterior,
    // sem creditar de novo. Sem a chave, mantém o comportamento simples de sempre.
    Account account = (idempotencyKey == null || idempotencyKey.isBlank())
        ? deposit.deposit(id, request.amount())
        : idempotency.executeOnce(idempotencyKey, () -> deposit.deposit(id, request.amount()));

    return ResponseEntity.ok(AccountResponse.from(account));
  }
}
