package br.com.digital.wallet.infrastructure;

import br.com.digital.wallet.application.AccountRepository;
import br.com.digital.wallet.application.DepositService;
import br.com.digital.wallet.application.OpenAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Composição de dependências (Spring). Fica na infraestrutura para manter a camada de
 * aplicação livre de framework: os casos de uso são POJOs; é aqui que viram beans.
 */
@Configuration
class WalletBeansConfiguration {

  @Bean
  OpenAccountService openAccountService(AccountRepository accounts) {
    return new OpenAccountService(accounts);
  }

  @Bean
  DepositService depositService(AccountRepository accounts) {
    return new DepositService(accounts);
  }
}
