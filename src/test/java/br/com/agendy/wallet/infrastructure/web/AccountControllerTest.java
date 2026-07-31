package br.com.agendy.wallet.infrastructure.web;

import br.com.agendy.wallet.application.OpenAccountService;
import br.com.agendy.wallet.domain.Account;
import br.com.agendy.wallet.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private OpenAccountService openAccount;

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
}
