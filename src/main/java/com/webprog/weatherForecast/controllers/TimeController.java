package com.webprog.weatherForecast.controllers;

import com.webprog.weatherForecast.services.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для предоставления текущего времени через API.
 */
@RestController
@RequestMapping("/api/time")
public class TimeController {

    @Autowired
    private TimeService timeService;

    /**
     * Обработчик GET-запроса для получения текущего времени.
     *
     * @return Строка с текущим временем.
     */
    @GetMapping()
    public String getCurrentTime() {
        return timeService.getCurrentTime();
    }
}