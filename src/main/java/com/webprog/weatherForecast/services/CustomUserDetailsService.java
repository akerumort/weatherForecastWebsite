package com.webprog.weatherForecast.services;

import com.webprog.weatherForecast.models.User;
import com.webprog.weatherForecast.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис для загрузки пользовательских данных для Spring Security.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает пользовательские данные по электронной почте для Spring Security.
     *
     * @param email Электронная почта пользователя.
     * @return Данные пользователя, реализующие интерфейс UserDetails.
     * @throws UsernameNotFoundException Если пользователь не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Не найден пользователь с адресом: " + email);

        }
        return user;
    }
}
