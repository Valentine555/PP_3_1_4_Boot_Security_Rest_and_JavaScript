package ru.kata.spring.boot_security.demo.configs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.kata.spring.boot_security.demo.services.UserService;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {
    private final SuccessUserHandler successUserHandler;
    private final UserService userService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserService userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index", "/login").permitAll() // Разрешаем доступ всем
                        .requestMatchers("/user").hasAnyRole("USER", "ADMIN") // Доступ для USER и ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Доступ только для ADMIN
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .formLogin(form -> form
                        .successHandler(successUserHandler) // Используем кастомный обработчик успешной аутентификации
                        .permitAll() // Разрешаем доступ к форме входа всем
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true") // Перенаправление на страницу входа после выхода
                        .permitAll() // Разрешаем доступ к выходу всем
                ).authenticationProvider(authenticationProvider());
              //  .userDetailsService(userDetailsService); // Указываем UserDetailsService
        return http.build();
    }

    // InMemory-аутентификация (пользователи хранятся в памяти)
    //@Bean
    //public UserDetailsService userDetailsService() {
      //  UserDetails user = User.builder()
        //        .username("user")
          //      .password(passwordEncoder().encode("user")) // Кодируем пароль
            //    .roles("USER")
              //  .build();

        //UserDetails admin = User.builder()
          //      .username("admin")
            //    .password(passwordEncoder().encode("admin")) // Кодируем пароль
              //  .roles("USER","ADMIN")
                //.build();

        //return new InMemoryUserDetailsManager(user, admin); // Создаем менеджер пользователей в памяти
    //}

    //сверщик-у юзердетейлсервиса запрашивает юзера и сравнивает совпадают данные или нет, если да, кладет в  контекст
    @Bean
    public DaoAuthenticationProvider authenticationProvider() { //мы отдали логин и пароль и его задача сказать существует такой пользователь или нет, и положить его в спрингсекьюритиконтекст
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder()); // Указываем PasswordEncoder
        authProvider.setUserDetailsService(userService); // Указываем UserDetailsService-предоставляет юзеров
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Шифрование паролей
    }
}
