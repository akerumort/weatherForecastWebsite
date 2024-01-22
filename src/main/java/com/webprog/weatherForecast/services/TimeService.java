package com.webprog.weatherForecast.services;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Сервис для предоставления текущего времени.
 */
@Service
public class TimeService {

    /**
     * Получает текущее время в формате "HH:mm:ss".
     *
     * @return Текущее время в виде строки.
     */
    public String getCurrentTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return localDateTime.format(dateTimeFormatter);
    }
}