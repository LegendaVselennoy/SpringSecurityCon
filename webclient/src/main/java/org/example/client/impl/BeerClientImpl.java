package org.example.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.client.BeerClient;
import org.example.model.BeerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class BeerClientImpl implements BeerClient {

    public static final String BEER_PATH = "/beer";
    public static final String BEER_PATH_ID = BEER_PATH+ "/{beerId}";
    private final WebClient webClient;

    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<BeerDTO> updateBeer(BeerDTO beerDTO) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(beerDTO.getId()))
                .body(Mono.just(beerDTO), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> getBeerById(beerDTO.getId()));
    }

    @Override
    public Mono<BeerDTO> createBeer(BeerDTO beerDTO) {
        return webClient.post()
                .uri(BEER_PATH)
                .body(Mono.just(beerDTO), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponse ->
                        Mono.just(voidResponse.getHeaders().get("Location").get(0)))
                .map(path -> path.split("/")[path.split("/").length-1])
                .flatMap(this::getBeerById);
    }

    @Override
    public Flux<BeerDTO> getBeerByBeerStyle(String beerStyle) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BEER_PATH)
                        .queryParam("beerStyle", beerStyle).build())
                .retrieve()
                .bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> getBeerById(String id) {
        return webClient.get()
                .uri(uriBuilder ->
                    uriBuilder.path(BEER_PATH_ID)
                            .build(id))
                .retrieve()
                .bodyToMono(BeerDTO.class);
    }

    @Override
    public Flux<BeerDTO> listBeerDto() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(BeerDTO.class);
    }

    @Override
    public Flux<JsonNode> listBeersJsonNode() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(JsonNode.class);
    }

    @Override
    public Flux<Map> listBeerMap() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(Map.class);
    }

    @Override
    public Flux<String> listBeer() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(String.class);
    }
}
