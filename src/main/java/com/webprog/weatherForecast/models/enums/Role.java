package com.webprog.weatherForecast.models.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Перечисление, представляющее роли пользователей в системе.
 * Реализует интерфейс GrantedAuthority для интеграции с Spring Security.
 */
public enum Role implements GrantedAuthority {

    /**
     * Роль пользователя.
     */
    ROLE_USER,

    /**
     * Роль модератора.
     */
    ROLE_MODER,

    /**
     * Роль администратора.
     */
    ROLE_ADMIN;

    /**
     * Получает строковое представление роли.
     *
     * @return Строковое представление роли.
     */
    @Override
    public String getAuthority() {
        return name();
    }
}
