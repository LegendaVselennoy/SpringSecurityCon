package org.example.beer.bootstrap;

import lombok.RequiredArgsConstructor;
import org.example.beer.domain.Beer;
import org.example.beer.repository.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class BootStrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
    }

    private void loadBeerData() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {

                Beer beer1 = Beer.builder()
                        .beerName("Mango")
                        .beerStyle("ALE")
                        .upc("89")
                        .price(new BigDecimal("18.29"))
                        .quantityOnHand(20)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer2 = Beer.builder()
                        .beerName("Cat")
                        .beerStyle("PALE_ALE")
                        .upc("12345")
                        .price(new BigDecimal("14.00"))
                        .quantityOnHand(12)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer3 = Beer.builder()
                        .beerName("Bar")
                        .beerStyle("IPA")
                        .upc("78")
                        .price(new BigDecimal("15.75"))
                        .quantityOnHand(17)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                beerRepository.save(beer1).subscribe();
                beerRepository.save(beer2).subscribe();
                beerRepository.save(beer3).subscribe();
            }
        });
    }
}
