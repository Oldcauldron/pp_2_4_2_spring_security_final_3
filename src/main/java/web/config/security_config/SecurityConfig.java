package web.config.security_config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService; // сервис, с помощью которого тащим пользователя
    private final SuccessUserHandler successUserHandler; // класс, в котором описана логика перенаправления пользователей по ролям

    public SecurityConfig(@Qualifier("userDetailsServiceImpl")
                          UserDetailsService userDetailsService,
                          SuccessUserHandler successUserHandler) {
        this.userDetailsService = userDetailsService;
        this.successUserHandler = successUserHandler;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        // конфигурация для прохождения аутентификации
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // http.csrf().disable(); - попробуйте выяснить сами, что это даёт
//        http.authorizeRequests()
//                // доступность всем
//                .antMatchers("/").permitAll()
//                // разрешаем входить на /user пользователям с ролью User
//                .antMatchers("/user").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
//                .antMatchers("/admin").access("hasAnyRole('ROLE_ADMIN')")
//                // Spring сам подставит свою логин форму
//                .and().formLogin()
//                // подключаем наш SuccessHandler для перенеправления по ролям
//                .successHandler(successUserHandler);
//    }
//
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                // указываем страницу с формой логина
                // не html, а путь контроллера, на него он error будет отправлять
                .loginPage("/logincustom")
                //указываем логику обработки при логине
//                .successHandler(new SuccessUserHandler()) // !!! ПРОВЕРИТЬ
                .successHandler(successUserHandler)
                // указываем action с формы логина
                .loginProcessingUrl("/logincustom")
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                // даем доступ к форме логина всем
                .permitAll();

        http.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/logincustom?logout")
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                .and().csrf().disable();

        http
                // делаем страницу регистрации недоступной для авторизированных пользователей
                .authorizeRequests()
                //страницы аутентификаци доступна всем
                .antMatchers("/logincustom").anonymous()
                .antMatchers("/").permitAll()
                .antMatchers("/user/**").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
                .antMatchers("/admin").access("hasAnyRole('ROLE_ADMIN')")
                // защищенные URL
                .antMatchers("/hello").access("hasAnyRole('ADMIN')")
                .anyRequest().authenticated();
    }

    // Необходимо для шифрования паролей
    // В данном примере не используется, отключен
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}

