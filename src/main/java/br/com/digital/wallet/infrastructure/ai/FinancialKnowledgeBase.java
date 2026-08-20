package br.com.digital.wallet.infrastructure.ai;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base de conhecimento de educação financeira, carregada em memória ao subir a aplicação.
 *
 * <p>A recuperação é <b>léxica</b> (sobreposição de termos), de propósito: a base é pequena
 * e curada, então um banco vetorial + embeddings seria over-engineering — e o tier gratuito
 * do Gemini só oferece o modelo de chat, não o de embeddings. A evolução natural, quando a
 * base crescer, é embeddings + pgvector, sem tocar na porta {@code FinancialAssistant}.
 */
@Component
class FinancialKnowledgeBase {

    private static final Logger log = LoggerFactory.getLogger(FinancialKnowledgeBase.class);

    /** Conectivos ignorados na pontuação, para não poluir a relevância. */
    private static final Set<String> STOPWORDS = Set.of(
            "de", "da", "do", "das", "dos", "no", "na", "nos", "nas", "em", "ao", "aos",
            "um", "uma", "uns", "umas", "que", "com", "por", "para", "sem", "sob", "sobre",
            "os", "as", "ou", "se", "sua", "seu", "suas", "seus", "qual", "quais", "como",
            "isso", "este", "esta", "esse", "essa", "entre", "meu", "minha");

    /** Um trecho recuperável da base, com a fonte para citação. */
    record Trecho(String fonte, String texto) {}

    private final List<Trecho> trechos = new ArrayList<>();

    @PostConstruct
    void carregar() {
        try {
            var resolver = new PathMatchingResourcePatternResolver();
            Resource[] arquivos = resolver.getResources("classpath:/educacao/*.md");
            for (Resource r : arquivos) {
                String nome = r.getFilename();
                String conteudo = r.getContentAsString(StandardCharsets.UTF_8);
                for (String secao : conteudo.split("(?m)^##\\s")) {
                    String texto = secao.strip();
                    if (!texto.isEmpty()) {
                        trechos.add(new Trecho(nome, texto));
                    }
                }
            }
            log.info("Base de educacao financeira carregada: {} arquivos, {} trechos.",
                    arquivos.length, trechos.size());
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao carregar a base de educacao financeira", e);
        }
    }

    /**
     * Recupera os {@code topK} trechos mais relevantes por sobreposição de termos:
     * conta quantas palavras da pergunta aparecem em cada trecho. Simples e explicável.
     * Retorna vazio quando nada casa — o adaptador trata isso como "sem contexto".
     */
    List<Trecho> buscar(String pergunta, int topK) {
        Set<String> termos = tokenizar(pergunta);
        if (termos.isEmpty()) {
            return List.of();
        }
        return trechos.stream()
                .map(t -> new Pontuado(t, pontuar(termos, t.texto())))
                .filter(p -> p.score() > 0)
                .sorted(Comparator.comparingInt(Pontuado::score).reversed())
                .limit(topK)
                .map(Pontuado::trecho)
                .toList();
    }

    private record Pontuado(Trecho trecho, int score) {}

    private static int pontuar(Set<String> termos, String texto) {
        Set<String> palavras = tokenizar(texto);
        int score = 0;
        for (String termo : termos) {
            for (String palavra : palavras) {
                if (casa(termo, palavra)) {
                    score++;
                    break;
                }
            }
        }
        return score;
    }

    /**
     * Dois termos casam se são iguais ou compartilham um prefixo de ao menos 5 letras.
     * Cobre variações morfológicas do português (diversificar/diversificação,
     * investimento/investimentos) sem precisar de um stemmer completo.
     */
    private static boolean casa(String a, String b) {
        if (a.equals(b)) {
            return true;
        }
        int n = Math.min(a.length(), b.length());
        int i = 0;
        while (i < n && a.charAt(i) == b.charAt(i)) {
            i++;
        }
        return i >= 5;
    }

    private static Set<String> tokenizar(String texto) {
        // remove acentos (NFD + tira os diacríticos) para a busca ser insensível a acento:
        // "diversificação" e "diversificacao" viram o mesmo termo.
        String semAcento = Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return Arrays.stream(semAcento
                        .replaceAll("[^\\p{L}\\p{Nd}\\s]", " ")
                        .split("\\s+"))
                .filter(p -> p.length() >= 2 && !STOPWORDS.contains(p))
                .collect(Collectors.toSet());
    }
}
