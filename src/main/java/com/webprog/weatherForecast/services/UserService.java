package com.webprog.weatherForecast.services;

import com.webprog.weatherForecast.models.User;
import com.webprog.weatherForecast.models.enums.Role;
import com.webprog.weatherForecast.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Сервис для базовой логики регистрации и управления пользователями.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Создает нового пользователя на основе данных из формы регистрации.
     *
     * @param user Объект пользователя, созданный из данных формы регистрации.
     * @return true, если пользователь успешно создан, false - если пользователь с таким email уже существует.
     */
    public boolean createUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false; // ручная валидация: пользователь с таким email уже существует
        user.setStatus(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_ADMIN);
        user.setFa2_enabled(false);
        log.info("Сохранен новый пользователь с email: {}", email);
        userRepository.save(user);
        return true;
    }

    /**
     * Сбрасывает параметры OTP (одноразового кода) пользователя, включает двухфакторную аутентификацию и сохраняет изменения в базе данных.
     *
     * @param user Пользователь, у которого нужно сбросить параметры OTP.
     */
    public void clearOTP(User user) {
        user.setFa2_enabled(true);
        user.setOneTimePassword(null);
        user.setOtpRequestedTime(null);
        userRepository.save(user);
    }

    /**
     * Получает пользователя по email.
     *
     * @param email Email пользователя.
     * @return Объект пользователя.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Получает список всех зарегистрированных пользователей.
     *
     * @return Список пользователей.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Меняет статус пользователя на противоположный (бан/разбан).
     *
     * @param userId ID пользователя, статус которого нужно изменить.
     */
    @Transactional // гарантирует, что обе операции (чтение и сохранение) будут выполнены как единая транзакция
    public void banUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setStatus(!user.isStatus());
        userRepository.save(user);
    }

    /**
     * Меняет роли пользователя.
     *
     * @param userId ID пользователя, роли которого нужно изменить.
     * @param roles  Множество новых ролей пользователя.
     */
    public void changeUserRoles(Long userId, Set<Role> roles) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setRoles(roles);
        userRepository.save(user);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param userId      ID пользователя, данные которого нужно обновить.
     * @param name        Новое имя пользователя.
     * @param email       Новый email пользователя.
     * @param phoneNumber Новый номер телефона пользователя.
     * @param password    Новый пароль пользователя.
     * @param fa2_enabled Новое значение для параметра двухфакторной аутентификации.
     */
    public void updateUserDetails(Long userId, String name, String email, String phoneNumber, String password, boolean fa2_enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setFa2_enabled(fa2_enabled);

        // Если поле пароля не пустое, обновляем его
        if (!password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);
        log.info("Данные пользователя с id={} успешно обновлены", userId);
    }

    /**
     * Получает пользователя по ID.
     *
     * @param id ID пользователя.
     * @return Объект пользователя.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с id " + id + " не найден"));
    }

    /**
     * Генерирует и устанавливает одноразовый код (OTP) для пользователя, отправляет его по электронной почте и сохраняет изменения в базе данных.
     *
     * @param user Пользователь, для которого нужно сгенерировать OTP.
     * @throws MessagingException        Исключение, связанное с отправкой электронных сообщений.
     * @throws UnsupportedEncodingException Исключение, связанное с неподдерживаемой кодировкой.
     */
    public void generateOneTimePassword(User user) throws MessagingException, UnsupportedEncodingException {
        String OTP = RandomString.make(6);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedOTP = passwordEncoder.encode(OTP);
        user.setOneTimePassword(encodedOTP);
        user.setOtpRequestedTime(new Date());

        user.setFa2_enabled(false);
        userRepository.save(user);

        sendOTPemail(user, OTP);
    }

    /**
     * Отправляет электронное сообщение с одноразовым кодом (OTP) для пользователя.
     *
     * @param user Пользователь, для которого нужно отправить OTP.
     * @param OTP  Одноразовый код, который будет отправлен пользователю.
     * @throws MessagingException        Исключение, связанное с отправкой электронных сообщений.
     * @throws UnsupportedEncodingException Исключение, связанное с неподдерживаемой кодировкой.
     */
    private void sendOTPemail(User user, String OTP) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("weatherforecasthelper@gmail.com", "Weather Forecast Support");
        helper.setTo(user.getEmail());

        String subject = "Подтверждение авторизации";
        String content = "<p> Здравствуйте " + user.getName() + ",</p>"
                + "<p> Ваш одноразовый код для входа: <b>" + OTP + "</b></p>"
                + "<p>Код истекает через 5 минут!</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
    }

    @Autowired
    JavaMailSender javaMailSender;
}
