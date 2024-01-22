package com.webprog.weatherForecast.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Сервис для получения данных о погоде с использованием OpenWeatherMap API.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherApiService {

    @Value("${weather.api.key}")
    private String apiKey;

    private static final String API_ENDPOINT = "https://api.openweathermap.org/data/2.5/weather";

    /**
     * Получает данные о погоде для указанного города.
     *
     * @param city Название города, для которого запрашиваются данные о погоде.
     * @return Строка в формате JSON с данными о погоде.
     * @throws IOException Возможное исключение в случае проблем при выполнении HTTP-запроса.
     */
    public String getWeatherByCity(String city) throws IOException {

        HttpClient httpClient = HttpClients.createDefault();

        try {
            // Кодируем название города перед добавлением к запросу (фиксим пробелы)
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());

            HttpGet httpGet = new HttpGet(API_ENDPOINT + "?q=" + encodedCity + "&appid=" + apiKey);

            HttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 404) {
                // Город не найден
                log.warn("Город не найден: {}", city);
                throw new RuntimeException("Город с названием:  '" + city + "' не найден");
                
            } else if (statusCode != 200) {
                // Обработка других ошибочных статусов
                log.error("Ошибка на стороне OpenWeatherMap API. Код ошибки: {}", statusCode);
                throw new RuntimeException("Ошибка на стороне OpenWeatherMap API. Код ошибки: " + statusCode);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error encoding city name", e);
        }
    }
}
