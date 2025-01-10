package com.example.SaleService.security.extractUserId;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ExtractUserIdArgumentResolver extractUserIdArgumentResolver;

    public WebMvcConfig(ExtractUserIdArgumentResolver extractUserIdArgumentResolver) {
        this.extractUserIdArgumentResolver = extractUserIdArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(extractUserIdArgumentResolver);
    }
}