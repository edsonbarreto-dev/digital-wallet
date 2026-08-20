package br.com.digital.wallet.infrastructure.ai;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Beans de infraestrutura da IA. O banco vetorial em memória mantém o exemplo simples
 * e sem infra externa (roda com um comando). Em produção, trocaria por pgvector/Redis
 * sem tocar na porta {@code FinancialAssistant}.
 */
@Configuration
class AiConfiguration {

    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }
}
