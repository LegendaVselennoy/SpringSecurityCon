package org.example.springguru.beer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.springguru.beer.model.dto.BeerDTO;
import org.example.springguru.beer.model.BeerStyle;
import org.example.springguru.beer.service.BeerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

    private Map<UUID, BeerDTO>beerMap;

    public BeerServiceImpl() {

        beerMap = new HashMap<>();

        BeerDTO beer1= BeerDTO.builder()
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

        BeerDTO beer2= BeerDTO.builder()
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


        BeerDTO beer3= BeerDTO.builder()
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
    public BeerDTO saveNewBeer(BeerDTO beer) {

        BeerDTO savedBeer= BeerDTO.builder()
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
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageName, Integer pageSize) {
//        return new ArrayList<>(beerMap.values());
        return new PageImpl<>(new ArrayList<>(beerMap.values()));
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        log.debug("getBeerById called");

        return Optional.of(beerMap.get(id));
    }

    @Override
    public Boolean deleteById(UUID beerId) {
        beerMap.remove(beerId);
        return true;
    }

    @Override
    public void patchById(UUID beerId, BeerDTO beer) {
        BeerDTO existing = beerMap.get(beerId);

        if (beer.getBeerName()!=null){
            existing.setBeerName(beer.getBeerName());
        }
        if (beer.getBeerStyle()!=null){
            existing.setBeerStyle(beer.getBeerStyle());
        }
        if (beer.getPrice()!=null){
            existing.setPrice(beer.getPrice());
        }
        if (beer.getQuantityOnHand()!=null){
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }
        if (beer.getUpc()!=null){
            existing.setUpc(beer.getUpc());
        }
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        BeerDTO existing=beerMap.get(beerId);
        existing.setBeerName(beer.getBeerName());
        existing.setPrice(beer.getPrice());
        existing.setUpc(beer.getUpc());
        existing.setQuantityOnHand(beer.getQuantityOnHand());

//        beerMap.put(existing.getBeerId(),existing);
        return Optional.of(existing);
    }
}
