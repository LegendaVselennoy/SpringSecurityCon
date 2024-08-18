package org.example.springguru.beer.service;

import org.example.springguru.beer.model.csv.BeerCSVRecord;
import org.example.springguru.beer.service.impl.BeerCsvServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BeerCsvServiceImplTest {

    BeerCsvServiceImpl beerCsvService = new BeerCsvServiceImpl();

    @Test
    void convertCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:beers.csv");

        List<BeerCSVRecord> recs = beerCsvService.convertCSV(file);

        assertThat(recs.size()).isGreaterThan(0);
    }
}
