package org.example.beer.repository;

import org.example.beer.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerRepository extends ReactiveCrudRepository<Beer,Integer> {
}