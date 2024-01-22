package com.webprog.weatherForecast.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webprog.weatherForecast.services.VisitService;
import com.webprog.weatherForecast.services.WeatherApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Контроллер для обработки запросов связанных с отображением информации о погоде.
 */
@Controller
public class WeatherController {

    private final WeatherApiService weatherApiService;
    private final ObjectMapper objectMapper;
    private final VisitService visitService;

    /**
     * Конструктор класса.
     *
     * @param weatherApiService Сервис для получения данных о погоде.
     * @param objectMapper      Объект для преобразования данных в формат JSON.
     * @param visitService      Сервис для отслеживания посещений страницы.
     */
    public WeatherController(WeatherApiService weatherApiService, ObjectMapper objectMapper, VisitService visitService) {
        this.weatherApiService = weatherApiService;
        this.objectMapper = objectMapper;
        this.visitService = visitService;
    }

    /**
     * Обрабатывает GET-запросы к корневому пути и отображает информацию о погоде.
     *
     * @param inputCity Название города, указанное в запросе. По умолчанию - "Пенза".
     * @param model     Модель для передачи данных в представление.
     * @return Имя представления, отображающего информацию о погоде.
     */
    @GetMapping("/")
    public String getWeather(@RequestParam(name = "city", defaultValue = "Москва") String inputCity, Model model) {
        int visitCount = visitService.getVisitCount();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = currentDate.format(formatterDate);
        String formattedTime = LocalTime.now().format(formatter);

        model.addAttribute("currentDate", formattedDate);
        model.addAttribute("formattedTime", formattedTime);
        model.addAttribute("visitCount", visitCount);
        model.addAttribute("title", "Погода");

        visitService.incrementVisitCount();

        try {
            // Извлекаем первый город до разделительного знака (запятая или точка)
            String city = inputCity.split("[,\\.]")[0].trim();

            String weatherData = weatherApiService.getWeatherByCity(city);

            JsonNode weatherJson = objectMapper.readTree(weatherData);
            double temperatureKelvin = weatherJson.path("main").path("temp").asDouble();
            long temperatureCelsius = Math.round(temperatureKelvin - 273.15);

            String description = weatherJson.path("weather").get(0).path("description").asText();

            model.addAttribute("city", city);
            model.addAttribute("temperature", temperatureCelsius);
            model.addAttribute("description", description);

        } catch (RuntimeException e) {
            // Обработка ошибок
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "weather";
    }

}
