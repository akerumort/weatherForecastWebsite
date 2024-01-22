package com.webprog.weatherForecast.controllers;

import com.webprog.weatherForecast.models.Post;
import com.webprog.weatherForecast.repos.PostRepository;
import lombok.RequiredArgsConstructor;
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
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Контроллер, отвечающий за функциональность пользователя с ролью модератора и выше.
 */
@Controller
@RequiredArgsConstructor
public class ModerController {

    private final PostRepository postRepository;

    /**
     * Обработчик GET-запроса для отображения панели модератора с постами.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для панели модератора.
     */
    @GetMapping("/moder/panel")
    public String moderPanel(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "moderPanel";
    }

    /**
     * Обработчик GET-запроса для отображения страницы редактирования поста модератором (администратором).
     *
     * @param id    Идентификатор поста, который модератор захочет отредактировать.
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для страницы редактирования поста модератором (администратором).
     */
    @GetMapping("/moder/{id}/edit")
    public String moderEdit(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "moderEdit";
    }

    /**
     * Обработчик POST-запроса для обновления информации о посте модератором (администратором).
     *
     * @param id      Идентификатор поста, который модератор (администратор) хочет обновить.
     * @param title   Заголовок поста.
     * @param anons   Анонс поста.
     * @param content Содержание поста.
     * @param file    Новый файл изображения для поста.
     * @param model   Модель для передачи данных в представление.
     * @return Строка перенаправления после обновления поста модератором (администратором).
     */
    @PostMapping("/moder/{id}/edit")
    public String moderPostUpdate(@PathVariable(value = "id") long id,
                                  @RequestParam String title,
                                  @RequestParam String anons,
                                  @RequestParam String content,
                                  @RequestParam("file") MultipartFile file,
                                  Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setContent(content);

        // Обработка загрузки нового изображения
        if (file != null && !file.isEmpty()) {
            try {
                if (post.getFilePath() != null) {
                    Path existingImagePath = Paths.get("uploads", post.getFilePath());
                    Files.deleteIfExists(existingImagePath);
                }

                // Сохранение нового файла на сервере
                String uploadDir = "uploads/";
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);

                // Создание директории
                if (!Files.exists(filePath.getParent())) {
                    Files.createDirectories(filePath.getParent());
                }

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                post.setFilePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        postRepository.save(post);
        return "redirect:/moder/panel";
    }

    /**
     * Обработчик POST-запроса для удаления поста модератором (администратором).
     *
     * @param id    Идентификатор поста, который модератор хочет удалить.
     * @param model Модель для передачи данных в представление.
     * @return Строка перенаправления после удаления поста модератором (администратором).
     */
    @PostMapping("/moder/{id}/delete")
    public String moderPostDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();

        if (post.getFilePath() != null) {
            Path existingImagePath = Paths.get("uploads", post.getFilePath());
            try {
                Files.deleteIfExists(existingImagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        postRepository.delete(post);
        return "redirect:/moder/panel";
    }

    /**
     * Обработчик POST-запроса для удаления изображения поста модератором (администратором).
     *
     * @param id    Идентификатор поста, изображение которого модератор (администратор) хочет удалить.
     * @param model Модель для передачи данных в представление.
     * @return Строка перенаправления после удаления изображения поста модератором (администратором).
     */
    @PostMapping("/moder/{id}/deleteImage")
    public String moderDeleteImage(@RequestParam long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();

        // Удаление изображения, если оно существует
        if (post.getFilePath() != null) {
            Path existingImagePath = Paths.get("uploads", post.getFilePath());
            try {
                Files.deleteIfExists(existingImagePath);
                post.setFilePath(null);
                postRepository.save(post);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/moder/panel";
    }
}
