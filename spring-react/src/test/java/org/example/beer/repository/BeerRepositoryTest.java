package org.example.beer.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.beer.BeerRepository;
import org.example.beer.config.DatabaseConfig;
import org.example.beer.domain.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

@DataR2dbcTest
@Import(DatabaseConfig.class)
public class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testCreateJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(mapper.writeValueAsString(getTestBeer()));
    }

    public static Beer getTestBeer() {
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(12)
                .upc("12345")
                .build();
    }

    @Test
    void saveNewBeer() {
        beerRepository.save(getTestBeer())
                .subscribe(System.out::println);
    }
}