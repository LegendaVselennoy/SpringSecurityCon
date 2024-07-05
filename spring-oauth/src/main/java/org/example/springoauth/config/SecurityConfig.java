package org.example.springoauth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
// сервер авторизации
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain asFilterChain(HttpSecurity http) throws Exception {
 // Вызов служебного метода для применения конфигураций по умолчанию для конечных точек сервера авторизации
        OAuth2AuthorizationServerConfiguration
                .applyDefaultSecurity(http);
 // Включение протокола OpenID Connect
        http.getConfigurer(
                OAuth2AuthorizationServerConfigurer.class)
                        .oidc(Customizer.withDefaults());
 // Указание страницы аутентификации для пользователей
        http.exceptionHandling((e)->
                e.authenticationEntryPoint(
                        new LoginUrlAuthenticationEntryPoint("/login")));

        return http.build();
    }

    @Bean
    @Order(2)
    // Мы устанавливаем фильтр, который будет интерпретироваться после одной конечной точки протокола
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    // Включаем метод аутентификации входа в форму
        http.formLogin(Customizer.withDefaults());
    // Мы настраиваем все конечные точки так, чтобы они требовали аутентификации.
        http.authorizeHttpRequests(
                c->c.anyRequest().authenticated()
        );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails= User.withUsername("user")
                .password("123")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Создание экземпляра RegisteredClient
//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        RegisteredClient registeredClient=RegisteredClient
//                .withId(UUID.randomUUID().toString())
//                .clientId("client")
//                .clientSecret("secret")
//                .clientAuthenticationMethod(
//                        ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(
////                        AuthorizationGrantType.AUTHORIZATION_CODE)
//                        AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .tokenSettings(TokenSettings.builder()
//                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
//                        .accessTokenTimeToLive(Duration.ofHours(12))
//                        .build())
////                .redirectUri("https://www.manning.com/authorized")
////                .scope(OidcScopes.OPENID)
//                .scope("CUSTOM")
//                .build();
//
//
//        // Определение экземпляра сведений о клиенте для сервера ресурсов (который также становится клиентом при вызове конечной точки интроспекции).
//        RegisteredClient resourceServer =
//                RegisteredClient.withId(UUID.randomUUID().toString())
//                        .clientId("resource_server")
//                        .clientSecret("resource_server_secret")
//                        .clientAuthenticationMethod(
//                                ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                        .authorizationGrantType(
//                                AuthorizationGrantType.CLIENT_CREDENTIALS)
//                        .build();
//
//    // Добавление его для управления реализацией RegisteredClientRepository в памяти
//     //   Добавление обоих экземпляров сведений о клиенте в репозиторий сервера авторизации
//        return new InMemoryRegisteredClientRepository(registeredClient,resourceServer);
//    }

//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        var registeredClient = RegisteredClient
//                .withId(UUID.randomUUID().toString())
//                .clientId("client")
//                .clientSecret("secret")
//                .clientAuthenticationMethod(
//                        ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(
//                        AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("http://localhost:8080/login/oauth2/code/my_authorization_server")
//                .scope(OidcScopes.OPENID)
//                .build();
//        return new InMemoryRegisteredClientRepository(registeredClient);
//    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        var registeredClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("client")
                .clientSecret("secret")
                .clientAuthenticationMethod(
                        ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(
                        AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope(OidcScopes.OPENID)
                .build();
        return new InMemoryRegisteredClientRepository(registeredClient);
    }


    /**
     * Программная генерация пары открытого и закрытого ключей с использованием криптографического алгоритма RSA
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey publicKey= (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey= (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey=new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

  // Добавление пары ключей в набор, используемый сервером авторизации для подписи выдаваемых токенов
        JWKSet jwkSet=new JWKSet(rsaKey);
  // Обертывание набора ключей в реализацию JWKSource и возврат его для добавления в контекст Spring
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context->{
            JwtClaimsSet.Builder claims=context.getClaims();
            claims.claim("priority","HIGH");
        };
    }

//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        RegisteredClient registeredClient =
//                RegisteredClient.withId(UUID.randomUUID().toString())
//                        .clientId("client")
//                        .clientSecret("secret")
//                        .clientAuthenticationMethod(
    // Разрешение зарегистрированному клиенту использовать тип предоставления учетных данных клиента
//                                ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
    // Настройка области в соответствии с назначением запроса маркера доступа
//                        .scope("CUSTOM")
//                        .build();
//        return new InMemoryRegisteredClientRepository(registeredClient);
//    }
}

// Тест по пути http://localhost:8080/.well-known/openid-configuration
