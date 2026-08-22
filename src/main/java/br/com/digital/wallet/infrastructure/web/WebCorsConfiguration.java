package br.com.digital.wallet.infrastructure.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Libera CORS para o webapp Angular (microfrontend) em desenvolvimento.
 *
 * <p>Usa origens explícitas (nunca {@code *}) — o shell roda em 4200 e o remoto em 4201.
 * Em produção, isso viria de configuração por ambiente com o domínio real do webapp.
 */
@Configuration
class WebCorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200", "http://localhost:4201")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
