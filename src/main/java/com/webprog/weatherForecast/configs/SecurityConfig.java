package com.webprog.weatherForecast.configs;

import com.webprog.weatherForecast.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурационный класс для настройки безопасности веб-приложения с использованием Spring Security.
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final CustomUserDetailsService userDetailsService;
    /**
     * Настраивает правила безопасности HTTP-запросов.
     *
     * @param http Объект конфигурации безопасности HTTP-запросов.
     * @throws Exception Возможное исключение при конфигурации.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().and()
                .authorizeRequests()
                .antMatchers("/", "/login", "/signUp", "/about", "/js/**", "/css/**", "/uploads/**", "/api/time").permitAll()
                .antMatchers("/user/profile").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/moder/**").hasAnyAuthority("ROLE_MODER", "ROLE_ADMIN")
                .antMatchers("/blog/add").hasAnyAuthority("ROLE_MODER", "ROLE_ADMIN")
                .antMatchers("/blog/{id}/edit").hasAnyAuthority("ROLE_MODER", "ROLE_ADMIN")
                .antMatchers("/blog/{id}/delete").hasAnyAuthority("ROLE_MODER", "ROLE_ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(beforeAuthenticationFilter, BeforeAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .defaultSuccessUrl("/", true)
                .and()
                .logout().permitAll();
    }

    /**
     * Настраивает аутентификацию пользователей.
     *
     * @param auth Объект конфигурации аутентификации.
     * @throws Exception Возможное исключение при конфигурации.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Создает бин для шифрования паролей.
     *
     * @return Объект PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    /**
     * Создает бин для обработчика неудачной аутентификации.
     *
     * @return Объект AuthenticationFailureHandler.
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private BeforeAuthenticationFilter beforeAuthenticationFilter;
}
