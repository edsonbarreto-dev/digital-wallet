package br.com.agendy.wallet.application;

import br.com.agendy.wallet.domain.Account;
import br.com.agendy.wallet.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class DepositServiceTest {

  private final Map<UUID, Account> store = new HashMap<>();

  private final AccountRepository accounts = new AccountRepository() {
    @Override
    public Account save(Account account) {
      store.put(account.id(), account);
      return account;
    }

    @Override
    public Optional<Account> findById(UUID id) {
      return Optional.ofNullable(store.get(id));
    }
  };

  @Test
  void deposito_aumenta_o_saldo_e_persiste() {
    Account conta = Account.open();
    store.put(conta.id(), conta);
    DepositService service = new DepositService(accounts);

    Account resultado = service.deposit(conta.id(), new BigDecimal("100.00"));

    assertThat(resultado.balance()).isEqualTo(Money.of(new BigDecimal("100.00")));
    assertThat(accounts.findById(conta.id()).orElseThrow().balance())
      .isEqualTo(Money.of(new BigDecimal("100.00")));
  }
}
