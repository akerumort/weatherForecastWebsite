package com.webprog.weatherForecast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения для запуска Spring Boot приложения "WeatherForecastApplication".
 */
@SpringBootApplication
public class WeatherForecastApplication {

	/**
	 * Метод main, используемый для запуска Spring Boot приложения.
	 *
	 * @param args Аргументы командной строки.
	 */
	public static void main(String[] args) {
		SpringApplication.run(WeatherForecastApplication.class, args);
	}

}
