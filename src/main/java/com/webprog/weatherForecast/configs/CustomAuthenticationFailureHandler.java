package com.webprog.weatherForecast.configs;

import com.webprog.weatherForecast.models.User;
import com.webprog.weatherForecast.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс-обработчик неудачной аутентификации для пользовательской аутентификации.
 * Расширяет SimpleUrlAuthenticationFailureHandler из Spring Security.
 * Предоставляет дополнительную логику обработки неудачной аутентификации, включая обработку ошибок,
 * связанных с блокировкой учетных записей, использованием одноразового пароля и неправильными учетными данными.
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private UserService userService;

    /**
     * Обрабатывает событие неудачной аутентификации, устанавливает URL для перенаправления и сообщение об ошибке.
     *
     * @param request     HTTP-запрос
     * @param response    HTTP-ответ
     * @param exception   Исключение аутентификации
     * @throws IOException      Исключение ввода/вывода
     * @throws ServletException Исключение сервлета
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String failureRedirectURL = "/login?error";
        String errorMessage="";
        if (!exception.getMessage().contains("Bad credentials")) {
            if (exception.getMessage().contains("User is disabled")) {
                errorMessage = "Ваш аккаунт заблокирован. Обратитесь к администратору для разблокировки.";
            }

            String email = request.getParameter("username");
            if (exception.getMessage().contains("OTP")) {
                failureRedirectURL = "/login?otp=true&email=" + email;
            }
        }
        else {
            String email = request.getParameter("username");
            errorMessage = "Неправильное имя пользователя или пароль";
            User user = userService.getUserByEmail(email);
            if (user!=null && user.isOTPrequied()) {
                failureRedirectURL = "/login?otp=true&email=" + email;
                errorMessage = "Неверный код подтверждения";
            }
        }

        if (!errorMessage.isEmpty()) {
            request.getSession().setAttribute("errorMessage", errorMessage);
        } else {
            // Удаление атрибута "errorMessage" из сессии при успешной аутентификации
            request.getSession().removeAttribute("errorMessage");
        }

        super.setDefaultFailureUrl(failureRedirectURL);
        // Метод суперкласса, который обрабатывает перенаправление пользователя на страницу по умолчанию после неудачной аутентификации
        super.onAuthenticationFailure(request, response, exception);
    }
}

