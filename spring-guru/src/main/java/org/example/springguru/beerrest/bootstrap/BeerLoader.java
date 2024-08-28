package org.example.springguru.beerrest.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springguru.beerrest.domain.BeerRest;
import org.example.springguru.beerrest.domain.BeerStyleEnum;
import org.example.springguru.beerrest.repository.BeerRepositoryRest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerLoader implements CommandLineRunner {

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0637734200036";
    public static final String BEER_3_UPC = "0631234110036";
    private final BeerRepositoryRest beerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerObjects();
    }

    private synchronized void loadBeerObjects() {
        log.info("Loading initial data. Count is: {}", beerRepository.count());

        if (beerRepository.count() == 0) {

            Random random = new Random();

            beerRepository.save(BeerRest.builder()
                    .beerName("Mango")
                    .beerStyle(BeerStyleEnum.ALE)
                    .upc(BEER_1_UPC)
                    .price(new BigDecimal(BigInteger.valueOf(random.nextInt(10000)), 2))
                    .quantityOnHand(random.nextInt(5000))
                    .build());

            beerRepository.save(BeerRest.builder()
                    .beerName("Cat")
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .upc(BEER_2_UPC)
                    .price(new BigDecimal(BigInteger.valueOf(random.nextInt(10000)), 2))
                    .quantityOnHand(random.nextInt(5000))
                    .build());

            beerRepository.save(BeerRest.builder()
                    .beerName("Bar")
                    .beerStyle(BeerStyleEnum.IPA)
                    .upc(BEER_3_UPC)
                    .price(new BigDecimal(BigInteger.valueOf(random.nextInt(10000)), 2))
                    .quantityOnHand(random.nextInt(5000))
                    .build());
        }
    }
}