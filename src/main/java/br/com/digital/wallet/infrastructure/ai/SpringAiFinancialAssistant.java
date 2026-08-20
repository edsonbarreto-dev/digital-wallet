package br.com.digital.wallet.infrastructure.ai;

import br.com.digital.wallet.application.AssistantAnswer;
import br.com.digital.wallet.application.FinancialAssistant;
import br.com.digital.wallet.infrastructure.ai.FinancialKnowledgeBase.Trecho;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador da porta {@link FinancialAssistant} usando Spring AI + Gemini, com RAG.
 *
 * Fluxo explícito (mostra domínio da mecânica, não só a "mágica"):
 *   1. Recupera da base os trechos mais relevantes para a pergunta (recuperação léxica).
 *   2. Monta um contexto só com esses trechos.
 *   3. Pede ao modelo que responda USANDO SÓ o contexto, com dois guardrails:
 *      não inventar e NUNCA recomendar investimento (apenas educar).
 *   4. Devolve a resposta + as fontes citadas.
 */
@Component
class SpringAiFinancialAssistant implements FinancialAssistant {

    private static final String SISTEMA = """
            Você é um assistente de EDUCAÇÃO financeira. Regras obrigatórias:
            - Explique conceitos de forma didática, usando apenas os TRECHOS fornecidos.
            - NUNCA dê recomendação de investimento. Não diga "compre", "invista em" nem
              "aloque". Isto é educação, não recomendação. Se pedirem recomendação, explique
              o conceito envolvido e oriente a procurar um profissional certificado pela CVM.
            - Se a resposta não estiver nos TRECHOS, responda exatamente:
              "Não tenho essa informação no material disponível."
            - Ao final, cite a(s) fonte(s) entre colchetes, ex.: [renda-fixa-vs-variavel.md].
            - Responda em português, de forma clara e objetiva.
            """;

    private final ChatClient chatClient;
    private final FinancialKnowledgeBase base;

    SpringAiFinancialAssistant(ChatClient.Builder builder, FinancialKnowledgeBase base) {
        this.chatClient = builder.build();
        this.base = base;
    }

    @Override
    public AssistantAnswer ask(String question) {
        List<Trecho> trechos = base.buscar(question, 4);

        String contexto = trechos.stream()
                .map(t -> "[" + t.fonte() + "]\n" + t.texto())
                .collect(Collectors.joining("\n\n---\n\n"));

        String resposta = chatClient.prompt()
                .system(SISTEMA)
                .user(u -> u.text("""
                        TRECHOS (material de educação financeira):
                        {contexto}

                        PERGUNTA: {pergunta}
                        """)
                        .param("contexto", contexto.isBlank() ? "(vazio)" : contexto)
                        .param("pergunta", question))
                .call()
                .content();

        List<String> fontes = trechos.stream()
                .map(Trecho::fonte)
                .distinct()
                .toList();

        return new AssistantAnswer(resposta, fontes);
    }
}
