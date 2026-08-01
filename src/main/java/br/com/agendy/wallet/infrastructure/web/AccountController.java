package br.com.agendy.wallet.infrastructure.web;

import br.com.agendy.wallet.application.DepositService;
import br.com.agendy.wallet.application.OpenAccountService;
import br.com.agendy.wallet.domain.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
class AccountController {

  private final OpenAccountService openAccount;
  private final DepositService deposit;

  AccountController(OpenAccountService openAccount, DepositService deposit) {
    this.openAccount = openAccount;
    this.deposit = deposit;
  }

  @PostMapping
  ResponseEntity<AccountResponse> open() {
    Account account = openAccount.open();
    return ResponseEntity
      .created(URI.create("/accounts/" + account.id()))
      .body(AccountResponse.from(account));
  }

  @PostMapping("/{id}/deposits")
  ResponseEntity<AccountResponse> deposit(@PathVariable UUID id, @RequestBody DepositRequest request) {
    Account account = deposit.deposit(id, request.amount());
    return ResponseEntity.ok(AccountResponse.from(account));
  }
}
