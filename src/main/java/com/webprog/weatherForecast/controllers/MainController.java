package com.webprog.weatherForecast.controllers;

import com.webprog.weatherForecast.models.Post;
import com.webprog.weatherForecast.repos.PostRepository;
import com.webprog.weatherForecast.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер, отвечающий за обработку запросов, связанных с главной страницей, блогом и авторизацией.
 */
@Controller
public class MainController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private VisitService visitService;
    /**
     * Обработчик GET-запроса для отображения страницы "О нас".
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для страницы "О нас".
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "О нас");
        return "about";
    }

    /**
     * Обработчик GET-запроса для отображения страницы входа в систему.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для страницы входа.
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Войти");
        return "login";
    }

    /**
     * Обработчик GET-запроса для отображения страницы блога.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для главной страницы блога.
     */
    @GetMapping("/blog")
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll(); // достаем все посты из БД
        model.addAttribute("posts", posts);
        return "blog";
    }

    /**
     * Обработчик GET-запроса для выхода пользователя из системы.
     *
     * @return Редирект на страницу входа с параметром "logout".
     */
    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/login?logout";
    }
}