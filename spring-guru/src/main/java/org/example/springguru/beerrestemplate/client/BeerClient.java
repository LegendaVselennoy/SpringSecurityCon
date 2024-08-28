package org.example.springguru.beerrestemplate.client;

import org.example.springguru.beerrestemplate.model.BeerDTORestam;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerClient {
    Page<BeerDTORestam> listBeers(String beerName);

    BeerDTORestam getBeerById(UUID beerId);

    BeerDTORestam createBeer(BeerDTORestam newDto);

    BeerDTORestam updateBeer(BeerDTORestam beerDto);

    void deleteBeer(UUID beerId);
}
