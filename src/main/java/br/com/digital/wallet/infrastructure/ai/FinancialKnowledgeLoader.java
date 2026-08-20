package br.com.digital.wallet.infrastructure.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Ingestão do RAG: ao subir a aplicação, indexa a base de educação financeira
 * (resources/educacao/*.md) no banco vetorial, guardando a "fonte" de cada trecho.
 */
@Component
class FinancialKnowledgeLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(FinancialKnowledgeLoader.class);

    private final VectorStore vectorStore;

    FinancialKnowledgeLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var resolver = new PathMatchingResourcePatternResolver();
        Resource[] arquivos = resolver.getResources("classpath:/educacao/*.md");

        int total = 0;
        for (Resource r : arquivos) {
            String nome = r.getFilename();
            String conteudo = r.getContentAsString(StandardCharsets.UTF_8);
            List<Document> trechos = quebrarPorSecao(conteudo, nome);
            vectorStore.add(trechos);
            total += trechos.size();
            log.info("Indexado: {} ({} trechos)", nome, trechos.size());
        }
        log.info("Base de educacao financeira carregada: {} arquivos, {} trechos.",
                arquivos.length, total);
    }

    /**
     * Chunking simples por seção de markdown (linhas iniciando com "## ").
     * Para uma base pequena, quebrar por seção basta e mantém o contexto coeso.
     * Em bases grandes, aqui entraria um splitter por tokens (ex.: TokenTextSplitter).
     */
    private static List<Document> quebrarPorSecao(String conteudo, String fonte) {
        List<Document> trechos = new ArrayList<>();
        for (String secao : conteudo.split("(?m)^##\\s")) {
            String texto = secao.strip();
            if (!texto.isEmpty()) {
                trechos.add(new Document(texto, Map.of("fonte", fonte)));
            }
        }
        return trechos;
    }
}
