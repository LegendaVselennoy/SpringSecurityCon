package org.example.person.repository.impl;

import org.example.person.domain.Person;
import org.example.person.repository.PersonalRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonalRepositoryImpl implements PersonalRepository {

    Person star = Person.builder()
            .id(1)
            .firstName("Star")
            .lastName("Galaxy")
            .build();

    Person fiona = Person.builder()
            .id(2)
            .firstName("Fiona")
            .lastName("Galaxies")
            .build();

    Person alice = Person.builder()
            .id(3)
            .firstName("ALice")
            .lastName("Galaxies")
            .build();

    Person jessee = Person.builder()
            .id(4)
            .firstName("Jessee")
            .lastName("Galaxies")
            .build();


    @Override
    public Mono<Person> getById(Integer id) {
       return getFindByPersonId(id);
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(star, fiona, alice, jessee);
    }

    public Mono<Person> getFindByPersonId(Integer id) {
        return switch (id) {
            case 1 -> Mono.just(star);
            case 2 -> Mono.just(fiona);
            case 3 -> Mono.just(alice);
            case 4 -> Mono.just(jessee);
            default -> Mono.empty();
        };
    }
}