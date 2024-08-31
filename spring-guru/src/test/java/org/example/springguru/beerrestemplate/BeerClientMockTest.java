package org.example.springguru.beerrestemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springguru.beerrestemplate.client.BeerClient;
import org.example.springguru.beerrestemplate.config.OAuthClientInterceptor;
import org.example.springguru.beerrestemplate.config.RestTemplateBuilderConfig;
import org.example.springguru.beerrestemplate.model.BeerDTORestam;
import org.example.springguru.beerrestemplate.model.BeerStyleRestam;
import org.example.springguru.beerrestemplate.model.RestPageImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withAccepted;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@Import(RestTemplateBuilderConfig.class)
public class BeerClientMockTest {

    static final String URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/rest";
    private static final String GET_BEER_BY_ID_PATH = "/rest/{beerId}";
    public static final String BEARER_TEST = "Bearer test";

//    @Autowired
    BeerClient beerClient;
//    @Autowired
    MockRestServiceServer server;
    @Autowired
    RestTemplateBuilder restTemplateBuilder;
    @Autowired
    ObjectMapper objectMapper;
    @Mock
    RestTemplateBuilder mockRestTemplate = new RestTemplateBuilder(new MockServerRestTemplateCustomizer());
    BeerDTORestam dto;
    String dtoJson;

    @MockBean
    OAuth2AuthorizedClientManager manager;
    @Autowired
    ClientRegistrationRepository registrationRepository;
    @Autowired
    private InMemoryClientRegistrationRepository clientRegistrationRepository;

    @TestConfiguration
    public static class TestConfig {

        @Bean
        ClientRegistrationRepository clientRegistrationRepository() {
            return new InMemoryClientRegistrationRepository(
                    ClientRegistration.withRegistrationId("springauth")
                            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                            .clientId("test")
                            .tokenUri("test")
                            .build());
        }

        @Bean
        OAuth2AuthorizedClientService auth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
            return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
        }

        @Bean
        OAuthClientInterceptor oAuthClientInterceptor(
                @Qualifier("oAuth2AuthorizedClientManager") OAuth2AuthorizedClientManager manager,
                 ClientRegistrationRepository clientRegistrationRepository) {
            return new OAuthClientInterceptor(manager, clientRegistrationRepository);
        }
    }

    @BeforeEach
    void setUp() throws JsonProcessingException {
        ClientRegistration clientRegistration =
                clientRegistrationRepository.findByRegistrationId("springauth");

        OAuth2AccessToken token =
                new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,"test", Instant.MIN,Instant.MAX);

        when(manager.authorize(any())).thenReturn(new OAuth2AuthorizedClient(clientRegistration,"test",token));

        RestTemplate restTemplate = restTemplateBuilder.build();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        when(mockRestTemplate.build()).thenReturn(restTemplate);
        beerClient = new BeerClientImpl(mockRestTemplate);
        dto = getBeerDto();
        dtoJson = objectMapper.writeValueAsString(dto);
    }

    private void mockGetOperation() {
        server.expect(method(HttpMethod.GET))
                .andExpect(requestToUriTemplate(URL + GET_BEER_PATH,dto.getBeerId()))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));
    }

    @Test
    void testListBeers() throws JsonProcessingException {
       String payload = objectMapper.writeValueAsString(getPage());
       server.expect(method(HttpMethod.GET))
               .andExpect(requestTo(URL + GET_BEER_PATH))
               .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTORestam> dtos = beerClient.listBeers("ALE");
        assertThat(dtos.getContent().size()).isGreaterThan(0);
    }

    BeerDTORestam getBeerDto() {
       return BeerDTORestam.builder()
                .price(new BigDecimal("100.00"))
                .beerName("Mango")
                .beerStyle(BeerStyleRestam.PALE_ALE)
                .quantityOnHand(500)
                .upc("123")
                .build();
    }

    RestPageImpl getPage() {
        return new RestPageImpl(Arrays.asList(getBeerDto()),1,25,1);
    }

    @Test
    void testGetById() {
        mockGetOperation();

        BeerDTORestam responseDto = beerClient.getBeerById(dto.getBeerId());
        assertThat(responseDto.getBeerId()).isEqualTo(dto.getBeerId());
    }

    @Test
    void testUpdateBeer() {
        server.expect(method(HttpMethod.PUT))
                .andExpect(requestToUriTemplate(URL+GET_BEER_BY_ID_PATH,dto.getBeerId()))
                .andRespond(withNoContent());

        mockGetOperation();

        BeerDTORestam responseDto = beerClient.updateBeer(dto);
        assertThat(responseDto.getBeerId()).isEqualTo(dto.getBeerId());
    }

    @Test
    void testCreateBeer() {
        URI uri = UriComponentsBuilder.fromPath(GET_BEER_BY_ID_PATH).build(dto.getBeerId());

        server.expect(method(HttpMethod.POST))
                .andExpect(requestTo(URL + GET_BEER_PATH))
                .andRespond(withAccepted().location(uri));

        mockGetOperation();

        BeerDTORestam responseDto = beerClient.createBeer(dto);
        assertThat(responseDto.getBeerId()).isEqualTo(dto.getBeerId());
    }

    @Test
    void testListBeersWithQueryParam() throws JsonProcessingException {
        String response = objectMapper.writeValueAsString(getPage());

        URI uri = UriComponentsBuilder.fromHttpUrl(GET_BEER_PATH)
                .queryParam("beerName","ALE")
                .build().toUri();

        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(uri))
                .andExpect(header("Authorization","BASIC"))
                .andExpect(requestTo(uri))
                .andExpect(queryParam("beerName","ALE"))
                .andRespond(withSuccess(response,MediaType.APPLICATION_JSON));

        Page<BeerDTORestam> responsePage = beerClient.listBeers("ALE");
        assertThat(responsePage.getContent().size()).isEqualTo(1);
    }

    @Test
    void testDeleteBeer() {
        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestToUriTemplate(URL+GET_BEER_BY_ID_PATH,dto.getBeerId()))
                .andRespond(withNoContent());

        beerClient.deleteBeer(dto.getBeerId());

        server.verify();
    }

    @Test
    void testDeleteNotFound() {
        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestToUriTemplate(URL+GET_BEER_BY_ID_PATH,dto.getBeerId()))
                .andRespond(withResourceNotFound());

        assertThrows(HttpClientErrorException.class, () ->{
            beerClient.deleteBeer(dto.getBeerId());
        });
        server.verify();
    }

}
