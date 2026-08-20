package br.com.digital.wallet.application;

import java.util.function.Supplier;

/**
 * Porta de idempotência: garante que uma operação identificada por uma chave seja
 * aplicada <b>no máximo uma vez</b>. Se a mesma chave chegar de novo (reentrega ou
 * duplicata), devolve o <b>resultado anterior</b> sem reexecutar o efeito.
 *
 * <p>É a porta que resolve o problema clássico de webhook/comando duplicado: a PSP
 * reentrega em timeout, e sem idempotência o efeito (creditar, cobrar) seria aplicado
 * duas vezes.
 */
public interface IdempotencyRegistry {

    /**
     * Executa {@code operacao} apenas se {@code chave} for nova; caso a chave já tenha
     * sido processada, devolve o resultado já computado, sem reexecutar.
     */
    <T> T executeOnce(String chave, Supplier<T> operacao);
}
