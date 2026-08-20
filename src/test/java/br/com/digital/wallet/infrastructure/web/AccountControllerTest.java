package br.com.digital.wallet.infrastructure.web;

import br.com.digital.wallet.application.DepositService;
import br.com.digital.wallet.application.IdempotencyRegistry;
import br.com.digital.wallet.application.OpenAccountService;
import br.com.digital.wallet.domain.Account;
import br.com.digital.wallet.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private OpenAccountService openAccount;

  @MockitoBean
  private DepositService deposit;

  @MockitoBean
  private IdempotencyRegistry idempotency;

  @Test
  void post_accounts_cria_conta_retorna_201_com_location_e_corpo() throws Exception {
    UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
    when(openAccount.open()).thenReturn(Account.restore(id, Money.of(BigDecimal.ZERO), 0L));

    mvc.perform(post("/accounts"))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", "/accounts/" + id))
      .andExpect(jsonPath("$.id").value(id.toString()))
      .andExpect(jsonPath("$.balance").value("0.00"));
  }

  @Test
  void post_deposits_credita_e_retorna_200_com_saldo_atualizado() throws Exception {
    UUID id = UUID.fromString("22222222-2222-2222-2222-222222222222");
    when(deposit.deposit(eq(id), any(BigDecimal.class)))
      .thenReturn(Account.restore(id, Money.of(new BigDecimal("100.00")), 1L));

    mvc.perform(post("/accounts/{id}/deposits", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"amount\": 100.00}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(id.toString()))
      .andExpect(jsonPath("$.balance").value("100.00"));
  }
}
