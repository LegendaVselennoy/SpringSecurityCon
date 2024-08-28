package org.example.springguru.beerrest.repository;

import org.example.springguru.beerrest.domain.BeerRest;
import org.example.springguru.beerrest.domain.BeerStyleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(path = "rest", collectionResourceRel = "rest")
public interface BeerRepositoryRest extends JpaRepository<BeerRest, UUID> {

    Page<BeerRest> findAllByBeerName(String beerName, Pageable pageable);
    Page<BeerRest> findAllByBeerStyle(BeerStyleEnum beerStyle, Pageable pageable);
    Page<BeerRest> findAllByBeerNameAndBeerStyle(String beerName, BeerStyleEnum beerStyle, Pageable pageable);
    BeerRest findByUpc(String upc);
}
