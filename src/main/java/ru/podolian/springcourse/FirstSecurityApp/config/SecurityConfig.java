package ru.podolian.springcourse.FirstSecurityApp.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.podolian.springcourse.FirstSecurityApp.services.PersonDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Включаем @PreAuthorize/@PostAuthorize
// (современная аннотация вместо @EnableGlobalMethodSecurity)
public class SecurityConfig {

    private final PersonDetailsService personDetailsService; // сервис, реализующий UserDetailsService

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService; // внедряем PersonDetailsService через конструктор
    }

    /**
     * Основная цепочка безопасности — заменяет устаревший configure(HttpSecurity)
     * Возвращаем SecurityFilterChain, который Spring применит к входящим запросам.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Конфигурация правил доступа (новый DSL)
                .authorizeHttpRequests(auth -> auth
                        // Доступ только пользователям с ролью ADMIN на /admin
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // Эти страницы доступны всем (страница логина, регистрация, /error)
                        .requestMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                        // Все остальные URL доступны пользователям с ролями USER или ADMIN
                        .anyRequest().hasAnyRole("USER", "ADMIN")
                )
                // Конфигурация формы логина (кастомная страница логина)
                .formLogin(form -> form
                        .loginPage("/auth/login") // URL кастомной страницы логина (контроллер + шаблон должны быть)
                        .loginProcessingUrl("/process_login")  // URL, на который браузер шлёт POST с credentials
                        .defaultSuccessUrl("/hello", true)
                        // Куда редиректить после успешной аутентификации
                        .failureUrl("/auth/login?error") // Куда редиректить при ошибке логина
                        .permitAll() // разрешаем доступ к странице логина всем
                )
                // Конфигурация логаута
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL выхода (POST по умолчанию)
                        .logoutSuccessUrl("/auth/login") // Куда редиректить после logout
                );

        return http.build(); // Строим и возвращаем SecurityFilterChain
    }

    /**
     * AuthenticationManager — тот компонент, который отвечает за проверку логина/пароля.
     * Здесь мы регистрируем personDetailsService и PasswordEncoder в AuthenticationManager.
     * Это замена configure(AuthenticationManagerBuilder auth) из старого API.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // Получаем билдер аутентификации из HttpSecurity
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        // Регистрируем UserDetailsService и PasswordEncoder — Spring создаст DaoAuthenticationProvider под капотом
        authBuilder
                .userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());

        // Строим и возвращаем AuthenticationManager
        return authBuilder.build();
    }

    /**
     * PasswordEncoder бин. В продакшне **обязательно** использовать BCrypt (или другой стойкий хэш).
     * Если у тебя в базе хранятся raw/plain пароли (учебный проект),
     * и ты не хочешь менять их — используй NoOpPasswordEncoder,
     * но это **небезопасно** и только для разработки/учебы.
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(); // безопасный шифратор паролей
        // Для отладки/учебы (если пароли plain): return NoOpPasswordEncoder.getInstance();
    }
}



