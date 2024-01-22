package com.webprog.weatherForecast.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурационный класс для настройки обработки статических ресурсов в Spring MVC.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * Добавляет обработчик статических ресурсов для указанного пути.
     *
     * @param registry Реестр обработчиков ресурсов для настройки.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
    }
}