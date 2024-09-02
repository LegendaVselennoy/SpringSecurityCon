package org.example.person.repository;

import org.example.person.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonalRepository {

    Mono<Person> getById(Integer id);
    Flux<Person> findAll();
}
