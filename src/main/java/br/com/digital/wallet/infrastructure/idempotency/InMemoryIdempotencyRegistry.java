package br.com.digital.wallet.infrastructure.idempotency;

import br.com.digital.wallet.application.IdempotencyRegistry;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Adaptador em memória da porta {@link IdempotencyRegistry} (versão didática, roda com um
 * comando, sem infraestrutura).
 *
 * <p>O {@code computeIfAbsent} do {@link ConcurrentHashMap} é <b>atômico</b>: a operação só
 * executa se a chave for nova; chamadas concorrentes com a mesma chave <b>não duplicam</b>
 * o efeito — a segunda recebe o resultado da primeira.
 *
 * <p>Em produção, quem cumpre esse papel é um <b>INSERT com constraint de unicidade</b> na
 * chave, dentro da mesma transação do efeito: o banco garante a unicidade, e isso sobrevive
 * a restart e a múltiplas instâncias (o mapa em memória, não). Trocar um pelo outro <b>não
 * toca</b> na porta nem nos casos de uso — é só outro adaptador.
 */
@Component
class InMemoryIdempotencyRegistry implements IdempotencyRegistry {

    private final Map<String, Object> processados = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T executeOnce(String chave, Supplier<T> operacao) {
        return (T) processados.computeIfAbsent(chave, k -> operacao.get());
    }
}
