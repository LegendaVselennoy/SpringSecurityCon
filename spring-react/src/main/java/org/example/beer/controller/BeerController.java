package org.example.beer.controller;

import lombok.RequiredArgsConstructor;
import org.example.beer.model.BeerDTO;
import org.example.beer.service.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    public static final String URL = "http://localhost:8080";
    private final BeerService beerService;

    @GetMapping(BEER_PATH_ID)
    Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping(BEER_PATH)
    Flux<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @PutMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> updateExistingBeer(
//    ResponseEntity<Void> updateExistingBeer(
            @PathVariable("beerId") Integer beerId,
           @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.updateBeer(beerId,beerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(savedDto -> ResponseEntity.noContent().build());
//        beerService.updateBeer(beerId, beerDTO).subscribe();
//        return ResponseEntity.noContent().build();
    }

    @PostMapping(BEER_PATH)
    Mono<ResponseEntity<Void>> createNewBeer(@Validated @RequestBody BeerDTO beerDTO) {
       return beerService.saveNewBeer(beerDTO)
               .map(savedDto -> ResponseEntity.created(UriComponentsBuilder
                       .fromHttpUrl(URL + BEER_PATH + "/" + savedDto.getId())
                       .build().toUri())
                       .build());
    }

    @PatchMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchExistingBeer(
            @PathVariable("beerId") Integer beerId,
            @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.patchBeer(beerId,beerDTO)
                .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }

    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(beerDto -> beerService.deleteBeerById(beerDto.getId()))
                .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }
}
