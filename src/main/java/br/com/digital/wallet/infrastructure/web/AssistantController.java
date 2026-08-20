package br.com.digital.wallet.infrastructure.web;

import br.com.digital.wallet.application.AskFinancialQuestionService;
import br.com.digital.wallet.application.AssistantAnswer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assistant")
class AssistantController {

    private final AskFinancialQuestionService service;

    AssistantController(AskFinancialQuestionService service) {
        this.service = service;
    }

    @PostMapping("/perguntar")
    AssistantAnswer perguntar(@RequestBody Ask request) {
        return service.ask(request.pergunta());
    }

    record Ask(String pergunta) {}
}
