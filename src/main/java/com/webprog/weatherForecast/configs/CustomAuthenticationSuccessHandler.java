package com.webprog.weatherForecast.configs;

import com.webprog.weatherForecast.models.User;
import com.webprog.weatherForecast.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Обработчик успешной аутентификации пользователя.
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * Обработчик успешной аутентификации пользователя.
     *
     * @param request        Запрос от клиента
     * @param response       Ответ сервера
     * @param authentication Аутентификационный объект
     * @throws IOException      Возникает при ошибке ввода-вывода
     * @throws ServletException Возникает при ошибке в обработке запроса
     */
    @Autowired
    private UserService userService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        if (user.isOTPrequied()) {
            userService.clearOTP(user);
        }
        request.getSession().removeAttribute("errorMessage");
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
