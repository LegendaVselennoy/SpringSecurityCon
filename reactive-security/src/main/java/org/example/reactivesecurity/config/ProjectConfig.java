package org.example.reactivesecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.function.Function;

@Configuration
// Включает функцию безопасности реактивного метода
@EnableReactiveMethodSecurity
public class ProjectConfig {

    // Добавляет ReactiveUserDetailsService в контекст Spring
    @Bean
    public ReactiveUserDetailsService userDetailsService() {
    // Создает нового пользователя с его именем пользователя, паролем и правами доступа
        var u= User.withUsername("user")
                .password("123")
                .authorities("read")
                .build();

        // Создает MapReactiveUserDetailsService для управления экземплярами UserDetails
        var uds=new MapReactiveUserDetailsService(u);
        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.formLogin(Customizer.withDefaults());
        // Начинает настройку авторизации конечной точки
        http.authorizeExchange(
                // Выбирает запросы, для которых мы применяем правила авторизации
                c -> c.pathMatchers(HttpMethod.GET, "/hello")
                        .authenticated()
                // Относится к любому другому запросу
                        .anyExchange()
                        .permitAll()
        );

        return http.build();
    }

    private Mono<AuthorizationDecision> getAuthorizationDecisionMono(
    // Метод, определяющий пользовательское правило авторизации, получает Authentication и контекст запроса в качестве параметров.
            Mono<Authentication> a,
            AuthorizationContext c) {
 //  Из контекста он получает путь к запросу
        String path = getRequestPath(c);
        boolean restrictedTime =
                LocalTime.now().isAfter(LocalTime.NOON);

   //  Для пути /hello применяется пользовательское правило авторизации.
        if(path.equals("/hello")) {
            return a.map(isAdmin())
                    .map(auth -> auth && !restrictedTime)
                    .map(AuthorizationDecision::new);
        }
        return Mono.just(new AuthorizationDecision(false));
    }

    private String getRequestPath(AuthorizationContext c) {
        return c.getExchange()
                .getRequest()
                .getPath()
                .toString();
    }

    private Function<Authentication, Boolean> isAdmin() {
        return p ->
                p.getAuthorities().stream()
                        .anyMatch(e -> e.getAuthority().equals("ROLE_ADMIN"));
    }

}
