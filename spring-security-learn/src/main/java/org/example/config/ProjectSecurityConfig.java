package org.example.config;

import org.example.exception.CustomAccessDeniedHandler;
import org.example.exception.CustomBasicAuthenticationEntryPoint;
import org.example.filter.AuthoritiesLoggingAfterFilter;
import org.example.filter.AuthoritiesLoggingAtFilter;
import org.example.filter.CsrfCookieFilter;
import org.example.filter.JWTTokenGeneratorFilter;
import org.example.filter.JWTTokenValidatorFilter;
import org.example.filter.RequestValidationBeforeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http
                .securityContext(contextConfig -> contextConfig.requireExplicitSave(false))
//                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
//                    @Override
//                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                        CorsConfiguration config = new CorsConfiguration();
//                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
//                        config.setAllowedMethods(Collections.singletonList("*"));
//                        config.setAllowCredentials(true);
//                        config.setAllowedHeaders(Collections.singletonList("*"));
//                        config.setExposedHeaders(Arrays.asList("Authorization"));
//                        config.setMaxAge(3600L);
//                        return config;
//                    }
//                }))
//                .sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession")
//                        .maximumSessions(3).maxSessionsPreventsLogin(true))
                //.requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) // Only HTTPS
                //.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()) // Only HTTP
//                .csrf(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/register")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(
                (request) -> request
//                        .requestMatchers("/myAccount").hasAuthority("VIEW_ACCOUNT")
//                        .requestMatchers("/myBalance").hasAnyAuthority("VIEW_BALANCE","VIEW_ACCOUNT")
                        .requestMatchers("/myBalance").hasRole("USER")
                        .requestMatchers("/myAccount").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/security", "/dashboard").authenticated()
                        .requestMatchers("/register", "/invalidSession", "/login", "/register/apiLogin").permitAll())
//                        .formLogin(Customizer.withDefaults())
                .formLogin(flc -> flc.loginPage("/login").defaultSuccessUrl("/dashboard"))
                .httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()))
                .exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .logout(log -> log.logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true)
                        .clearAuthentication(true).deleteCookies("JSESSIONID"));
//                .exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()).accessDeniedPage("/denied"));
//                .exceptionHandling(ehc -> ehc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
//                        .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        UserDetails user = User.withUsername("user").password("12345").authorities("read").build();
//        UserDetails user = User.withUsername("user").password("{noop}12345").authorities("read").build();
//        UserDetails admin = User.withUsername("admin")
//                .password("{bcrypt}$2a$12$zO37cgOy79n//h9Nzk0i/OhtvT8s0HgzS4WxibXnaCr2s9rCzGWEK")
//                .authorities("admin").build();
//        return new InMemoryUserDetailsManager(user, admin);
//        return new JdbcUserDetailsManager(dataSource);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        UsernamePwdAuthenticationProvider authenticationProvider
                = new UsernamePwdAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

//    @Bean
//    public CompromisedPasswordChecker compromisedPasswordChecker() {
//        return new HaveIBeenPwnedRestApiPasswordChecker();
//    }
}
