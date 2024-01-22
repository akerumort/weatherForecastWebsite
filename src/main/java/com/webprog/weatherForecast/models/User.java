package com.webprog.weatherForecast.models;

import com.webprog.weatherForecast.models.enums.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс, представляющий сущность "Пользователь" в системе.
 * Реализует интерфейс UserDetails для использования с Spring Security.
 */
@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Уникальный адрес электронной почты пользователя.
     */
    @Column(name = "email", unique = true)
    private String email;

    /**
     * Номер телефона пользователя.
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Имя пользователя.
     */
    @Column(name = "name")
    private String name;

    /**
     * Статус активности пользователя.
     */
    @Column(name = "status")
    private boolean status;

    /**
     * Путь к аватару пользователя.
     */
    @Column(name = "avatar_path")
    private String avatarPath;

    /**
     * Пароль пользователя.
     */
    @Column(name = "password", length = 1000)
    public String password;

    /**
     * Включение/отключение двухфакторной аутентификации
     */
    @Column(name = "fa2_enabled")
    private boolean fa2_enabled;

    /**
     * OTP (одноразовый) код подтверждения
     */
    @Column(name = "one_time_password")
    private String oneTimePassword;

    /**
     * Время действия OTP пароля
     */
    @Column(name = "otp_requested_time")
    private Date otpRequestedTime;

    /**
     * Таблица с ролями пользователей.
     */
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
    joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    /**
     * Дата создания пользователя.
     */
    private LocalDateTime dateOfCreated;

    /**
     * Инициализация даты создания перед сохранением в базу данных.
     */
    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    /**
     * Получение коллекции ролей пользователя.
     *
     * @return Коллекция ролей пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /**
     * Получение пароля пользователя. В случае использования OTP, возвращает OTP, в противном случае - обычный пароль.
     *
     * @return Пароль пользователя.
     */
    @Override
    public String getPassword() {
        if (isOTPrequied()) {
            return oneTimePassword;
        }
        return password;
    }

    /**
     * Получение имени пользователя.
     *
     * @return Имя пользователя.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Проверка активности учетной записи пользователя.
     *
     * @return true, если учетная запись активна, в противном случае - false.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверка блокировки учетной записи пользователя.
     *
     * @return true, если учетная запись не заблокирована, в противном случае - false.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверка срока действия учетных данных пользователя.
     *
     * @return true, если учетные данные действительны, в противном случае - false.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверка активности учетной записи пользователя.
     *
     * @return true, если учетная запись активна, в противном случае - false.
     */
    @Override
    public boolean isEnabled() {
        return status;
    }

    /**
     * Получение значения переменной fa2_enabled.
     *
     * @return true, если двухфакторная аутентификация включена, в противном случае - false.
     */
    public boolean get_Fa2_enabled() {
        return fa2_enabled;
    }

    /**
     * Установка значения переменной fa2_enabled.
     *
     * @param fa2_enabled Значение, которое требуется установить для переменной fa2_enabled.
     */
    public void setFa2_enabled(boolean fa2_enabled) {
        this.fa2_enabled = fa2_enabled;
    }

    /**
     * Получение значения переменной oneTimePassword.
     *
     * @return Значение переменной oneTimePassword.
     */
    public String get_OneTimePassword() {
        return oneTimePassword;
    }

    /**
     * Установка значения переменной oneTimePassword.
     *
     * @param oneTimePassword Значение, которое требуется установить для переменной oneTimePassword.
     */
    public void setOneTimePassword(String oneTimePassword) {
        this.oneTimePassword = oneTimePassword;
    }

    /**
     * Получение значения переменной otpRequestedTime.
     *
     * @return Значение переменной otpRequestedTime.
     */
    public Date get_OtpRequestedTime() {
        return otpRequestedTime;
    }

    /**
     * Установка значения переменной otpRequestedTime.
     *
     * @param otpRequestedTime Значение, которое требуется установить для переменной otpRequestedTime.
     */
    public void setOtpRequestedTime(Date otpRequestedTime) {
        this.otpRequestedTime = otpRequestedTime;
    }

    /**
     * Проверка необходимости OTP. Если oneTimePassword равен null, возвращает false.
     * Если с момента запроса OTP прошло более OTP_VALID_DURATION, возвращает false, в противном случае - true.
     *
     * @return true, если OTP необходим, в противном случае - false.
     */
    public boolean isOTPrequied() {
        if (this.oneTimePassword == null) {
            return false;
        }
        long otp_req_time=this.otpRequestedTime.getTime();
        if (otp_req_time+OTP_VALID_DURATION<System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

    /**
     * Длительность действия OTP в миллисекундах (5 минут).
     */
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000;
}
