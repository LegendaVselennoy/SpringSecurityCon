package org.example.springguru.beer.service;

import org.example.springguru.beer.model.csv.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCsvService {
    List<BeerCSVRecord> convertCSV(File csvFile);
}
