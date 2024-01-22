package com.webprog.weatherForecast.configs;

import com.webprog.weatherForecast.models.User;
import com.webprog.weatherForecast.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Фильтр аутентификации, применяемый перед стандартным UsernamePasswordAuthenticationFilter Spring Security.
 * Проверяет, включена ли двухфакторная аутентификация для пользователя, и, если да, генерирует одноразовый пароль.
 */
@Component
public class BeforeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private UserService userService;

    /**
     * Конструктор класса BeforeAuthenticationFilter.
     * Устанавливает параметры имени пользователя и путь для аутентификации.
     */
    public BeforeAuthenticationFilter() {
        super.setUsernameParameter("username");
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login","POST"));
    }

    /**
     * Попытка аутентификации пользователя. Если у пользователя включена двухфакторная аутентификация,
     * генерирует одноразовый пароль и вызывает исключение InsufficientAuthenticationException с меткой "OTP".
     * В противном случае вызывает реализацию суперкласса.
     *
     * @param request  HTTP-запрос
     * @param response HTTP-ответ
     * @return Результат аутентификации
     * @throws AuthenticationException Исключение аутентификации
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("username");
        User user = userService.getUserByEmail(email);
        if (user!=null) {
            if (user.get_Fa2_enabled()) {
                super.attemptAuthentication(request, response);
                try {
                    userService.generateOneTimePassword(user);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    throw new RuntimeException("Ошибка отправки сообщения");
                }
                throw new InsufficientAuthenticationException("OTP");
            }
        }
        return super.attemptAuthentication(request,response);
    }

    /**
     * Установка менеджера аутентификации.
     *
     * @param authenticationManager Менеджер аутентификации
     */
    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    /**
     * Установка обработчика успешной аутентификации.
     *
     * @param successHandler Обработчик успешной аутентификации
     */
    @Autowired
    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    /**
     * Установка обработчика неудачной аутентификации.
     *
     * @param failureHandler Обработчик неудачной аутентификации
     */
    @Autowired
    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
            super.setAuthenticationFailureHandler(failureHandler);
    }
}
