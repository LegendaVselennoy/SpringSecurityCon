package org.example.springguru.beerrestemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {

//    private final ClientRegistrationRepository clientRegistrationRepository;
//    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
//
//    public RestTemplateBuilderConfig(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
//        this.clientRegistrationRepository = clientRegistrationRepository;
//        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
//    }

    @Value("${rest.template.rootUrl}")
    String rootUrl;
//    @Value("${rest.template.username}")
//    String username;
//    @Value("${rest.template.password}")
//    String password;

    @Bean
    OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        var auth2AuthorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager
                (clientRegistrationRepository, oAuth2AuthorizedClientService);
        auth2AuthorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return auth2AuthorizedClientManager;
    }

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer,
                                            OAuthClientInterceptor interceptor) {

        assert rootUrl != null;

       // RestTemplateBuilder builder = configurer.configure(new RestTemplateBuilder());
       // DefaultUriBuilderFactory uriBuilderFactory =
        //        new DefaultUriBuilderFactory(rootUrl);
       // RestTemplateBuilder builderWithAuth = builder.basicAuthentication(username,password);
       // return builderWithAuth.uriTemplateHandler(uriBuilderFactory);
        return configurer.configure(new RestTemplateBuilder())
//                .basicAuthentication(username, password)
                .additionalInterceptors(interceptor)
                .uriTemplateHandler(new DefaultUriBuilderFactory(rootUrl));
    }
}
