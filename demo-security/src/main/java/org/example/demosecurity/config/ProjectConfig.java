package org.example.demosecurity.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// сервер ресурсов
public class ProjectConfig {

 // Вставляет значение URI набора ключей в атрибут конфигурационного файла. Он понадобится для настройки цепи фильтров
//    @Value("${keySetURI}")
//    private String keySetUri;

    @Value("${introspectionUri}")
    private String introspectionUri;

    @Value("${resourceserver.clientID}")
    private String resourceServerClientID;

    @Value("${resourceserver.secret}")
    private String resourceServerSecret;

//    private final JwtAuthenticationConverter converter;
//
//    public ProjectConfig(JwtAuthenticationConverter converter) {
//        this.converter = converter;
//    }

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver(
            JwtDecoder jwtDecoder,
            OpaqueTokenIntrospector opaqueTokenIntrospector
    ) {

    // Определение диспетчера аутентификации для сервера авторизации, управляющего маркерами доступа JWT
        AuthenticationManager jwtAuth = new ProviderManager(
                new JwtAuthenticationProvider(jwtDecoder)
        );
    // Определение другого менеджера аутентификации для сервера авторизации, управляющего непрозрачными маркерами
        AuthenticationManager opaqueAuth = new ProviderManager(
                new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector)
        );
        return (request) -> {
    // Определение пользовательской логики сопоставителя диспетчера аутентификации для выбора диспетчера аутентификации на основе заголовка "type" HTTP-запроса
            if ("jwt".equals(request.getHeader("type"))) {
                return jwtAuth;
            } else {
                return opaqueAuth;
            }
        };
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
        // Настройка URI набора открытых ключей для диспетчера проверки подлинности, работающего с сервером авторизации, который управляет маркерами доступа JWT
                .withJwkSetUri("http://localhost:7070/oauth2/jwks")
                .build();
    }

    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector() {
        return new SpringOpaqueTokenIntrospector(
        // Настройка URI и учетных данных для диспетчера проверки подлинности, работающего с сервером авторизации, который управляет непрозрачными маркерами
                "http://localhost:6060/oauth2/introspect",
                "client", "secret");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // Настройка пользовательского сопоставителя диспетчера аутентификации
        http.oauth2ResourceServer(
                j -> j.authenticationManagerResolver(
                        authenticationManagerResolver(
                                jwtDecoder(),
                                opaqueTokenIntrospector()
                        ))
        );
        http.authorizeHttpRequests(
                c -> c.anyRequest().authenticated()
        );

        http.authorizeHttpRequests(
                c->c.anyRequest().authenticated()
        );

        return http.build();
    }



//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http)
//            throws Exception {
//
//        http.oauth2ResourceServer(
//                j -> j.authenticationManagerResolver(
//                        authenticationManagerResolver())
//        );
//        http.authorizeHttpRequests(
//                c -> c.anyRequest().authenticated()
//        );
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver() {
//
//        var a = new JwtIssuerAuthenticationManagerResolver(
//                "http://localhost:7070",
//                "http://localhost:8080");
//        return a;
//    }

//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        // Настройка приложения в качестве сервера ресурсов OAuth 2
//        http.oauth2ResourceServer(
//    // Настройка проверки подлинности сервера ресурсов для непрозрачных маркеров
//                c->c.opaqueToken(
//    // Настройка URI интроспекции, который сервер ресурсов должен использовать для проверки и получения сведений о маркерах.
//                        o->o.introspectionUri(introspectionUri)
//    // Настройка учетных данных, которые сервер ресурсов должен использовать для проверки подлинности при вызове URI интроспекции сервера авторизации
//                                .introspectionClientCredentials(
//                                        resourceServerClientID,
//                                        resourceServerSecret)
//                )
//        // Настройка сервера ресурсов для использования маркеров JWT для проверки подлинности
////                c->c.jwt(
//        // Настройка URL-адреса набора открытых ключей, который сервер ресурсов будет использовать для проверки маркеров
////                        j->j.jwkSetUri(keySetUri)
//        // Настройка объекта конвертера в механизме аутентификации
////                                .jwtAuthenticationConverter(converter)
//        );
//
//        http.authorizeHttpRequests(
//                c->c.anyRequest().authenticated()
//        );
//
//        return http.build();
//    }

}
