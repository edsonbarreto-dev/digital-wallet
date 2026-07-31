package br.com.agendy.wallet.infrastructure.web;

import br.com.agendy.wallet.application.OpenAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
class AccountController {

  private final OpenAccountService openAccount;

  AccountController(OpenAccountService openAccount) {
    this.openAccount = openAccount;
  }

  @PostMapping
  ResponseEntity<AccountResponse> open() {
    throw new UnsupportedOperationException("stub — TDD red do endpoint");
  }
}
