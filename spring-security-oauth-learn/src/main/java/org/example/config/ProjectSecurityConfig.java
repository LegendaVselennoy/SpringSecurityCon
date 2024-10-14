package org.example.config;

import org.example.converter.KeycloakRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

//    @Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-uri}")
//    String introspectionUri;
//    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
//    String clientId;
//    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
//    String clientSecret;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        http.authorizeHttpRequests(
                request -> request.requestMatchers("/secure").authenticated()
                        .anyRequest().permitAll())
                .oauth2ResourceServer(rsc -> rsc.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(
                        jwtAuthenticationConverter)));
//              .oauth2ResourceServer(rsc -> rsc.opaqueToken(otc -> otc.authenticationConverter(
//                      new KeycloakOpaqueRoleConverter()).introspectionUri(this.introspectionUri)
//                      .introspectionClientCredentials(this.clientId, this.clientSecret)));
//                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    ClientRegistrationRepository clientRegistrationRepository() {
//        ClientRegistration github = githubClientRegistration();
//        ClientRegistration facebook = facebookClientRegistration();
//        return new InMemoryClientRegistrationRepository(github, facebook);
//    }
//
//    private ClientRegistration githubClientRegistration() {
//       return CommonOAuth2Provider.GITHUB.getBuilder("github").clientId("Ov23liwMvE4DrROppumx")
//                .clientSecret("5b117dd639aa1763f7d0b9703d9497e86198e4cd").build();
//    }
//
//    private ClientRegistration facebookClientRegistration() {
//        return CommonOAuth2Provider.FACEBOOK.getBuilder("facebook").clientId("gghgh")
//                .clientSecret("fghghgh").build();
//    }
}
