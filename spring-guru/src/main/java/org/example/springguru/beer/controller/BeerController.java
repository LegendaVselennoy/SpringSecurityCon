package org.example.springguru.beer.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springguru.beer.model.Beer;
import org.example.springguru.beer.service.BeerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
//@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/beer")
public class BeerController {

    private final BeerService beerService;

    @PutMapping("/{beerId}")
    public ResponseEntity<Beer> updateBeer(@PathVariable UUID beerId, @RequestBody Beer beer) {
        beerService.updateBeerById(beerId,beer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Beer> handlePost(@RequestBody Beer beer) {
        Beer saved= beerService.saveNewBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location","/beer/"+saved.getBeerId().toString());
        return new ResponseEntity<>(headers,HttpStatus.CREATED);
    }

    @GetMapping
    public List<Beer>listBeers(){
        return beerService.listBeers();
    }

    @GetMapping("/{id}")
    public Beer getBeerId(@PathVariable UUID id){
        log.debug("Id get - in controller");
        return beerService.getBeerById(id);
    }
}
