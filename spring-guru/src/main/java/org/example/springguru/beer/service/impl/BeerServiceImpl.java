package org.example.springguru.beer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.springguru.beer.model.Beer;
import org.example.springguru.beer.model.BeerStyle;
import org.example.springguru.beer.service.BeerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

    private Map<UUID,Beer>beerMap;

    public BeerServiceImpl() {

        beerMap = new HashMap<>();

        Beer beer1=Beer.builder()
                .beerId(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12345")
                .price(new BigDecimal("7.77"))
                .quantityOnHand(121)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2=Beer.builder()
                .beerId(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("678910")
                .price(new BigDecimal("77.77"))
                .quantityOnHand(300)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();


        Beer beer3=Beer.builder()
                .beerId(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("1112131415")
                .price(new BigDecimal("5.77"))
                .quantityOnHand(145)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getBeerId(),beer1);
        beerMap.put(beer2.getBeerId(),beer2);
        beerMap.put(beer3.getBeerId(),beer3);
    }

    @Override
    public Beer saveNewBeer(Beer beer) {

        Beer savedBeer=Beer.builder()
                .beerId(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .build();

        beerMap.put(savedBeer.getBeerId(),savedBeer);

        return savedBeer;
    }

    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {

        log.debug("getBeerById called");

        return beerMap.get(id);
    }

    @Override
    public void updateBeerById(UUID beerId, Beer beer) {
        beerMap.put(beerId,beer);
    }
}
