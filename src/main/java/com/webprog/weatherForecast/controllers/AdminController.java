package com.webprog.weatherForecast.controllers;

import com.webprog.weatherForecast.models.User;
import com.webprog.weatherForecast.models.enums.Role;
import com.webprog.weatherForecast.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Контроллер, отвечающий за административные функции веб-приложения.
 */
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final UserService userService;
    /**
     * Обработчик GET-запроса для отображения административной панели.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для отображения административной панели.
     */
    @GetMapping("/admin/panel")
    public String adminPanel(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "adminPanel";
    }

    /**
     * Обработчик POST-запроса для блокировки пользователя (бана).
     *
     * @param userId     Идентификатор пользователя, которого необходимо заблокировать.
     * @param principal  Объект, представляющий текущего пользователя.
     * @return Строка перенаправления после выполнения блокировки пользователя.
     */
    @PostMapping("admin/ban/{userId}")
    public String banUser(@PathVariable Long userId, Principal principal) {

        // Получаем email текущего пользователя
        String currentUserEmail = principal.getName();
        // Получаем email пользователя, которого пытаемся забанить
        String targetUserEmail = userService.getUserById(userId).getEmail();

        // Проверяем, что текущий пользователь не пытается забанить самого себя
        if (currentUserEmail.equals(targetUserEmail)) {
            return "redirect:/admin/panel?error=self_ban";
        }
        userService.banUser(userId);
        return "redirect:/admin/panel";
    }

    /**
     * Обработчик POST-запроса для изменения ролей пользователя.
     *
     * @param userId     Идентификатор пользователя, у которого нужно изменить роли.
     * @param roleNames  Множество строк, представляющих названия ролей.
     * @return Строка перенаправления после выполнения изменения ролей пользователя.
     */
    @PostMapping("admin/assignRoles/{userId}")
    public String assignRoles(@PathVariable Long userId, @RequestParam Set<String> roleNames) {
        Set<Role> roles = roleNames.stream().map(Role::valueOf).collect(Collectors.toSet());
        userService.changeUserRoles(userId, roles);
        return "redirect:/admin/panel";
    }
}
