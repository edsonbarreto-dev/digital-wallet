package br.com.digital.wallet.infrastructure.web;

import java.math.BigDecimal;

/**
 * Corpo da requisição de depósito. A validação de valor (positivo, não-zero) é invariante
 * de domínio (Money/Account), então não a duplicamos aqui.
 */
record DepositRequest(BigDecimal amount) {
}
