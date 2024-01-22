package com.webprog.weatherForecast.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.webprog.weatherForecast.models.User;
import com.webprog.weatherForecast.repos.UserRepository;
import com.webprog.weatherForecast.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

/**
 * Контроллер, отвечающий за функциональность пользователя.
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * Обработчик GET-запроса для отображения страницы регистрации пользователя.
     *
     * @return Имя представления для страницы регистрации.
     */
    @GetMapping("/signUp")
    public String signUp() {
    return "signUp";
    }

    /**
     * Обработчик POST-запроса для создания нового пользователя.
     *
     * @param user  Объект пользователя, переданный из формы.
     * @param model Модель для передачи данных в представление.
     * @return Строка перенаправления после создания пользователя.
     */
    @PostMapping("/signUp")
    public String createUser(@ModelAttribute("user") User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с адресом" + user.getEmail() + " уже существует");
            return "signUp";
        }

        return "redirect:/login";
    }

    /**
     * Обработчик GET-запроса для отображения страницы профиля пользователя.
     *
     * @param model     Модель для передачи данных в представление.
     * @param principal Объект, представляющий текущего пользователя.
     * @return Имя представления для страницы профиля пользователя.
     */
    @GetMapping("/user/profile")
    public String userProfile(Model model, Principal principal) {
        // Получение данных текущего пользователя и передачи их на страницу
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "userProfile";
    }

    /**
     * Обработчик POST-запроса для обновления информации о пользователе.
     *
     * @param name         Имя пользователя.
     * @param email        Электронная почта пользователя.
     * @param phoneNumber  Номер телефона пользователя.
     * @param password     Пароль пользователя.
     * @param avatarFile   Файл с изображением для аватара пользователя.
     * @param principal    Объект, представляющий текущего пользователя.
     * @param model        Модель для передачи данных в представление.
     * @return Строка перенаправления после обновления профиля пользователя.
     */
    @PostMapping("/user/profile/updateDetails")
    public String updateUserDetails(@RequestParam String name,
                                    @RequestParam String email,
                                    @RequestParam String phoneNumber,
                                    @RequestParam(name = "fa2_enabled", defaultValue = "false") boolean fa2_enabled,
                                    @RequestParam String password,
                                    @RequestParam("avatar") MultipartFile avatarFile,
                                    Principal principal,
                                    Model model) {
        User user = userService.getUserByEmail(principal.getName());

        log.info("Текущая информация пользователя: {}", user);

        userService.updateUserDetails(user.getId(), name, email, phoneNumber, password,fa2_enabled);
        User updatedUser = userService.getUserByEmail(principal.getName());

        // Обработка загрузки аватара
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                if (user.getAvatarPath() != null) {
                    Path existingAvatarPath = Paths.get("uploads", user.getAvatarPath());
                    Files.deleteIfExists(existingAvatarPath);
                }

                // Сохранение нового файла на сервере
                String uploadDir = "uploads/";
                String fileName = UUID.randomUUID().toString() + "_" + avatarFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);

                // Создание директории
                if (!Files.exists(filePath.getParent())) {
                    Files.createDirectories(filePath.getParent());
                }

                Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                user.setAvatarPath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("Полученный файл: {}", avatarFile != null ? avatarFile.getOriginalFilename() : "Файл не был получен");
        userRepository.save(user);
        log.info("Обновленный профиль пользователя: {}", updatedUser);

        model.addAttribute("user", updatedUser);

        return "userProfile";
    }

    /**
     * Обработчик POST-запроса для удаления аватара пользователя.
     *
     * @param principal Объект, представляющий текущего пользователя.
     * @return Строка перенаправления после удаления аватара.
     */
    @PostMapping("/user/profile/deleteAvatar")
    public String deleteUserAvatar(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());

        // Удаление аватара, если он существует
        if (user.getAvatarPath() != null) {
            Path existingAvatarPath = Paths.get("uploads", user.getAvatarPath());
            try {
                Files.deleteIfExists(existingAvatarPath);
                user.setAvatarPath(null);
                userRepository.save(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/user/profile";
    }
}
