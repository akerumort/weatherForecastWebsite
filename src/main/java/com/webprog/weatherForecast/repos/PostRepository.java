package com.webprog.weatherForecast.repos;

import com.webprog.weatherForecast.models.Post;
import org.springframework.data.repository.CrudRepository;

/**
 * Интерфейс репозитория для взаимодействия с таблицей "Посты" в базе данных.
 */
public interface PostRepository extends CrudRepository<Post, Long> { // Для каждой модели нужно создавать свой репозиторий для манипулирования новых таблиц в БД

}
