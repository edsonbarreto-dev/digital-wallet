package br.com.digital.wallet.application;

/**
 * Caso de uso: responder uma dúvida de educação financeira.
 *
 * POJO, sem framework (o bean é montado na infraestrutura, como os demais casos de uso).
 * Orquestra a porta {@link FinancialAssistant} e concentra aqui a regra de aplicação
 * (ex.: pergunta não pode ser vazia). A mecânica de IA fica no adaptador.
 */
public class AskFinancialQuestionService {

    private final FinancialAssistant assistant;

    public AskFinancialQuestionService(FinancialAssistant assistant) {
        this.assistant = assistant;
    }

    public AssistantAnswer ask(String question) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("A pergunta não pode ser vazia");
        }
        return assistant.ask(question);
    }
}
