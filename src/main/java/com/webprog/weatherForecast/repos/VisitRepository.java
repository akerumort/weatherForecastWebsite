package com.webprog.weatherForecast.repos;

import com.webprog.weatherForecast.models.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс репозитория для взаимодействия с таблицей "Посещения" в базе данных.
 */
public interface VisitRepository extends JpaRepository<Visit, Long> {

}
