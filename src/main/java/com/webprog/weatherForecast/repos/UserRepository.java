package com.webprog.weatherForecast.repos;

import com.webprog.weatherForecast.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс репозитория для взаимодействия с таблицей "Пользователи" в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Поиск пользователя по электронной почте.
     *
     * @param email Электронная почта пользователя.
     * @return Найденный пользователь или null, если пользователь не найден.
     */
    User findByEmail(String email);
}
