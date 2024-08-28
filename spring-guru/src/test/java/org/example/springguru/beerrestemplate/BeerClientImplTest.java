package org.example.springguru.beerrestemplate;

import org.example.springguru.beerrestemplate.model.BeerDTORestam;
import org.example.springguru.beerrestemplate.model.BeerStyleRestam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void testDeleteBeer() {
        BeerDTORestam newDto = BeerDTORestam.builder()
                .price(new BigDecimal("100.00"))
                .beerName("Mango")
                .beerStyle(BeerStyleRestam.PALE_ALE)
                .quantityOnHand(500)
                .upc("123")
                .build();

        BeerDTORestam beerDto = beerClient.createBeer(newDto);
        beerClient.deleteBeer(beerDto.getBeerId());
        assertThrows(HttpClientErrorException.class,() -> {
            beerClient.getBeerById(beerDto.getBeerId());
        });
    }

    @Test
    void testCreateBeer() {
        BeerDTORestam newDto = BeerDTORestam.builder()
                .price(new BigDecimal("100.00"))
                .beerName("Mango")
                .beerStyle(BeerStyleRestam.PALE_ALE)
                .quantityOnHand(500)
                .upc("123")
                .build();

        BeerDTORestam saveDto = beerClient.createBeer(newDto);
        assertNotNull(saveDto);
    }

    @Test
    void testUpdateBeer() {
        BeerDTORestam newDto = BeerDTORestam.builder()
                .price(new BigDecimal("100.00"))
                .beerName("Mango")
                .beerStyle(BeerStyleRestam.IPA)
                .quantityOnHand(500)
                .upc("123")
                .build();

        BeerDTORestam beerDto = beerClient.createBeer(newDto);
        final String newName = "Mango S";
        beerDto.setBeerName(newName);
        BeerDTORestam updatedBeer = beerClient.updateBeer(beerDto);
        assertEquals(newName, updatedBeer.getBeerName());
    }

    @Test
    void getBeerById(){
        Page<BeerDTORestam>beerDTOS = beerClient.listBeers("ALE");
        BeerDTORestam dto = beerDTOS.getContent().get(0);
        BeerDTORestam byId = beerClient.getBeerById(dto.getBeerId());

        assertNotNull(byId);
    }

    @Test
    void listBeerNoName() {
        beerClient.listBeers(null);
    }

    @Test
    void listBeers() {
        beerClient.listBeers("ALE");
    }
}