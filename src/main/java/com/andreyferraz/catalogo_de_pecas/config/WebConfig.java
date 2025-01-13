package com.andreyferraz.catalogo_de_pecas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configura o diretório de uploads para ser acessível como recurso estático
        registry.addResourceHandler("/uploads/**") // Caminho acessível pela aplicação
                .addResourceLocations("file:C:/uploads/") 
                .setCachePeriod(3600); // Define tempo de cache (opcional, em segundos)
    }

}
