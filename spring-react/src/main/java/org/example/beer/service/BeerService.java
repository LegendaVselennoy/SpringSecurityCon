package org.example.beer.service;

import org.example.beer.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {

    Flux<BeerDTO> listBeers();

    Mono<BeerDTO> getBeerById(Integer beerId);

    Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO);

    Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDTO);

    Mono<Void> deleteBeerById(Integer beerId);

    Mono<BeerDTO> patchBeer(Integer beerId, BeerDTO beerDTO);
}
