package br.com.digital.wallet.application;

/**
 * Porta de saída: a capacidade de "assistente de educação financeira".
 *
 * Fala a língua da aplicação (pergunta -> resposta + fontes). Não conhece Spring AI,
 * Gemini nem HTTP — quem cumpre é um adaptador na borda (infrastructure), no mesmo
 * espírito do {@link AccountRepository}. Trocar o provedor de IA não toca no caso de uso.
 */
public interface FinancialAssistant {

    AssistantAnswer ask(String question);
}
