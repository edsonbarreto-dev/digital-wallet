package br.com.agendy.wallet.application;

import br.com.agendy.wallet.domain.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class OpenAccountServiceTest {

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
  void abrir_conta_persiste_e_devolve_conta_com_saldo_zero() {
    OpenAccountService service = new OpenAccountService(accounts);

    Account conta = service.open();

    assertThat(conta.id()).isNotNull();
    assertThat(conta.balance().amount()).isEqualByComparingTo(BigDecimal.ZERO);
    assertThat(accounts.findById(conta.id())).isPresent();
  }
}
