package org.example.repository.impl;

import org.example.person.domain.Person;
import org.example.person.repository.impl.PersonalRepositoryImpl;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonalRepositoryImplTest {

    PersonalRepositoryImpl personalRepository = new PersonalRepositoryImpl();

    @Test
    void testMonoByIdBlock() {
        Mono<Person> personMono = personalRepository.getById(1);

        Person person = personMono.block();

        assert person != null;
        System.out.println(person);
    }

    @Test
    void testGetByIdSubscriber() {
        Mono<Person> personMono = personalRepository.getById(1);

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testGetByIdNotFoundStepVerifier() {
        Mono<Person> personMono = personalRepository.getById(5);

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.getFirstName());
        });
    }

    @Test
    void testMapOperation() {
        Mono<Person> personMono = personalRepository.getById(1);

        personMono.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testGetById() {
        Mono<Person> fionaMono = personalRepository.findAll().filter(person -> person.getFirstName().equals("Fiona"))
                .next();

        fionaMono.subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void testGetByIdFound() {
        Mono<Person> personMono = personalRepository.getById(3);

        assertEquals(Boolean.TRUE, personMono.hasElement().block());
    }

    @Test
    void testGetByIdNotFound() {
        Mono<Person> personMono = personalRepository.getById(5);

        assertNotEquals(Boolean.TRUE, personMono.hasElement().block());
    }

    @Test
    void testGetByIdFoundStepVerifier() {
        Mono<Person> personMono = personalRepository.getById(3);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono.subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personalRepository.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId().equals(id)).single()
                .doOnError(throwable -> {
                    System.out.println(throwable.getMessage());
                });

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        }, throwable -> System.out.println("Error: " + throwable.getMessage()));
    }

    @Test
    void testFluxBlockFirst() {
        Flux<Person> personFlux = personalRepository.findAll();

        Person person = personFlux.blockFirst();

        System.out.println(person);
    }

    @Test
    void testFilterOnName() {
        personalRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona"))
                .subscribe(person -> System.out.println(person.getLastName()));
    }

    @Test
    void testFluxToList() {
        Flux<Person> personFlux = personalRepository.findAll();

        Mono<List<Person>> listMono = personFlux.collectList();

        listMono.subscribe(list -> list.forEach(person -> System.out.println(person.getFirstName())));
    }

    @Test
    void testFluxMap() {
        Flux<Person> personFlux = personalRepository.findAll();

        personFlux.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testFluxSubscriber() {
        Flux<Person> personFlux = personalRepository.findAll();

        personFlux.subscribe(System.out::println);
    }
}