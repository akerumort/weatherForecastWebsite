package com.webprog.weatherForecast.controllers;

import com.webprog.weatherForecast.models.Post;
import com.webprog.weatherForecast.repos.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * Контроллер, отвечающий за управление блогом веб-приложения.
 */
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_MODER', 'ROLE_ADMIN')")
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    /**
     * Обработчик GET-запроса для отображения страницы добавления нового поста в блог.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для страницы добавления нового поста.
     */
    @GetMapping("/blog/add")
    public String blogAdd(Model model) { return "blogAdd";}

    /**
     * Обработчик POST-запроса для добавления нового поста в блог.
     *
     * @param title   Заголовок поста.
     * @param anons   Анонс поста.
     * @param content Содержание поста.
     * @param file    Загружаемый файл.
     * @param model   Модель для передачи данных в представление.
     * @return Строка перенаправления после добавления поста.
     */
    @PostMapping("/blog/add")
    // @RequestParam - принимаемые параметры (введенные)
    public String blogAddPost(
            @RequestParam String title,
            @RequestParam String anons,
            @RequestParam String content,
            @RequestParam("file") MultipartFile file,
            Model model
    ) {
        try {
            Post post = new Post(title, anons, content);

            // Обработка загрузки файла к посту
            if (file != null && !file.isEmpty()) {
                String uploadDir = "uploads/";
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);

                // Создание директории
                if (!Files.exists(filePath.getParent())) {
                    Files.createDirectories(filePath.getParent());
                }
                Files.copy(file.getInputStream(), filePath);

                // Сохранение пути к файлу в базе данных
                post.setFilePath(fileName);
            }
            postRepository.save(post);
            return "redirect:/blog";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Ошибка при загрузке файла");
            return "blogAdd";
        }
    }

    /**
     * Обработчик GET-запроса для отображения конкретного поста.
     *
     * @param id    Идентификатор поста.
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для отображения деталей поста.
     */
    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> result = new ArrayList<>();

        // переводим из Optional в ArrayList
        post.ifPresent(result::add); // result содержит нужную запись
        model.addAttribute("post", result);
        return "blogDetails";
    }

    /**
     * Обработчик POST-запроса для обновления информации о посте.
     *
     * @param id      Идентификатор поста.
     * @param title   Заголовок поста.
     * @param anons   Анонс поста.
     * @param content Содержание поста.
     * @param model   Модель для передачи данных в представление.
     * @return Строка перенаправления после обновления поста.
     */
    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String content, Model model) {
        Post post = postRepository.findById(id).orElseThrow(); // orElseThrow() выбрасывает исключение в случае если запись не была найдена
        post.setTitle(title);
        post.setAnons(anons);
        post.setContent(content);

        // передаем СУЩЕСТВУЮЩИЙ объект
        postRepository.save(post);

        return "redirect:/blog";
    }

    /**
     * Обработчик POST-запроса для удаления поста.
     *
     * @param id    Идентификатор поста.
     * @param model Модель для передачи данных в представление.
     * @return Строка перенаправления после удаления поста.
     */
    @PostMapping("/blog/{id}/delete")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }

}
