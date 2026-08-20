# 02 — Assistente de Educação Financeira (IA)

Feature de IA adicionada ao Digital Wallet: um **assistente de educação financeira** que
responde dúvidas com base em uma base de conhecimento curada, usando **RAG**
(Retrieval-Augmented Generation) com **Spring AI 2.0 + Google Gemini** (free tier).

## Por que essa peça
- **Java 21 / Spring Boot 4** — stack-alvo do currículo.
- **Arquitetura hexagonal respeitada**: a IA entra como **porta de saída**
  (`FinancialAssistant`, na camada de aplicação) e o Spring AI vive **só no adaptador**
  (`infrastructure/ai`). Trocar Gemini por outro provedor não toca no caso de uso.
- **Confiável em produção**: dois guardrails — não inventa (responde só pela base e cita a
  fonte) e **nunca recomenda investimento** (apenas educa; orienta procurar profissional
  certificado pela CVM). Compliance-awareness é sinal de sênior.

## Arquitetura da feature
- `application/FinancialAssistant` — porta de saída (interface).
- `application/AskFinancialQuestionService` — caso de uso (POJO, montado no `WalletBeansConfiguration`).
- `infrastructure/ai/SpringAiFinancialAssistant` — adaptador: retrieval + geração + guardrails.
- `infrastructure/ai/FinancialKnowledgeLoader` — indexa `resources/educacao/*.md` ao subir.
- `infrastructure/ai/AiConfiguration` — bean do banco vetorial (em memória).
- `infrastructure/web/AssistantController` — `POST /assistant/perguntar`.

## Como rodar (local, sem infra externa)

### 1. Chave grátis do Gemini
Gere em **https://aistudio.google.com/apikey** (só conta Google, sem cartão).
```bash
export GEMINI_API_KEY="sua-chave"
```
> Free tier: informe **só** a `api-key`. Não configure `project-id`/`location`
> (isso força o modo Vertex AI, pago, e recusa a chave gratuita).

### 2. Suba com o perfil `dev` (H2 em memória, zero banco pra instalar)
```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```
Nos logs você verá a ingestão: `Indexado: renda-fixa-vs-variavel.md (N trechos)`.

### 3. Teste
```bash
curl -X POST http://localhost:8080/assistant/perguntar \
  -H "Content-Type: application/json" \
  -d '{"pergunta":"Qual a diferença entre renda fixa e renda variável?"}'
```
Perguntas boas para demonstrar:
- "O que é o FGC e qual o limite?" → R$ 250 mil por CPF/instituição.
- "O que é diversificação?" → explica o conceito, cita a fonte.
- "Qual ação eu devo comprar?" → **recusa educadamente** e explica que não dá
  recomendação (guardrail de compliance funcionando).
- "Qual a cotação do dólar agora?" → "não tenho essa informação no material disponível".

## Notas
- Banco vetorial `SimpleVectorStore` (em memória) de propósito, para rodar com um comando.
  Evolução natural: **pgvector** (o projeto já usa Postgres em produção).
- Spring AI evolui rápido; se algum método divergir da versão usada, veja
  `spring.ai.google.genai.*` na doc do Spring AI 2.0.

## Frase para a entrevista
> "Adicionei um assistente de educação financeira com RAG (Spring AI + Gemini),
> respeitando a arquitetura hexagonal: a IA é uma porta de saída e o Spring AI só existe no
> adaptador da borda. Coloquei guardrails de produção — citação de fontes e recusa a dar
> recomendação de investimento, por conformidade. O foco foi IA confiável, não brinquedo."
