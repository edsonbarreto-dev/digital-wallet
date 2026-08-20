package br.com.digital.wallet.application;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * O caso de uso é testado isoladamente, mockando a porta {@link FinancialAssistant}.
 * Mesma disciplina de TDD do restante do projeto: a aplicação não depende de IA real.
 */
class AskFinancialQuestionServiceTest {

    @Test
    void delega_a_pergunta_ao_assistente_e_devolve_a_resposta() {
        FinancialAssistant assistant = mock(FinancialAssistant.class);
        AssistantAnswer esperado = new AssistantAnswer("Renda fixa é...", List.of("renda-fixa-vs-variavel.md"));
        when(assistant.ask("o que é renda fixa?")).thenReturn(esperado);

        AskFinancialQuestionService service = new AskFinancialQuestionService(assistant);

        assertEquals(esperado, service.ask("o que é renda fixa?"));
    }

    @Test
    void rejeita_pergunta_vazia_sem_chamar_o_assistente() {
        FinancialAssistant assistant = mock(FinancialAssistant.class);
        AskFinancialQuestionService service = new AskFinancialQuestionService(assistant);

        assertThrows(IllegalArgumentException.class, () -> service.ask("   "));
        verifyNoInteractions(assistant);
    }
}
