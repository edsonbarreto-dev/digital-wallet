package br.com.agendy.wallet.infrastructure.web;

import br.com.agendy.wallet.application.OpenAccountService;
import br.com.agendy.wallet.domain.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/accounts")
class AccountController {

  private final OpenAccountService openAccount;

  AccountController(OpenAccountService openAccount) {
    this.openAccount = openAccount;
  }

  @PostMapping
  ResponseEntity<AccountResponse> open() {
    Account account = openAccount.open();
    return ResponseEntity
      .created(URI.create("/accounts/" + account.id()))
      .body(AccountResponse.from(account));
  }
}
