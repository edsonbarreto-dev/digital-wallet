package br.com.digital.wallet.application;

import java.util.List;

/**
 * Resultado do assistente: a resposta educacional e as fontes (documentos) que a
 * embasaram. As fontes tornam a resposta auditável — parte do "IA confiável".
 */
public record AssistantAnswer(String resposta, List<String> fontes) {
}
