package org.example.springguru.beer.repository;

import jakarta.validation.ConstraintViolationException;
import org.example.springguru.beer.model.BeerStyle;
import org.example.springguru.beer.model.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerListByName() {
        Page<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null);

        assertThat(list.getContent().size()).isEqualTo(336);
    }

    @Test
    void testSaveBeer(){

        assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.save(
                    Beer.builder()
                            .beerName("New Beer Long Name Exception Error")
                            .beerStyle(BeerStyle.PALE_ALE)
                            .upc("123456789")
                            .price(new BigDecimal("17.99"))
                            .build());

            beerRepository.flush();
        });

//        assertThat(savedBeer).isNotNull();
//        assertThat(savedBeer.getBeerId()).isNotNull();
    }

}