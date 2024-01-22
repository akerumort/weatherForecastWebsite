package com.webprog.weatherForecast.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс Spring для создания бина ObjectMapper.
 */
@Configuration
public class AppConfig {


    /**
     * Создает и возвращает бин ObjectMapper, который используется для преобразования объектов в JSON и обратно.
     *
     * @return Объект ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
