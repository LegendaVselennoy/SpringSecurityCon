package org.example.controller;

import org.example.dto.EmployeeDto;
import org.example.repository.EmployeeRepository;
import org.example.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository repository;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        repository.deleteAll().subscribe();
         employeeDto = new EmployeeDto(
                null,
                "Lege",
                "Vse",
                "r@ema.com"
        );
    }

    @Disabled("Error")
    @DisplayName("Test update")
    @Test
    void testUpdateEmployee() {
        EmployeeDto employeeDto1 = new EmployeeDto(
                null,
                "Lee",
                "Vsea",
                "p@ma.com"
        );
      EmployeeDto saveEmployee = employeeService.saveEmployee(employeeDto1).block();

        EmployeeDto updatedEmployee = new EmployeeDto(
                null,
                "Lee",
                "Vsea",
                "p@ma.com"
        );

        webTestClient.put()
                .uri("/employee/{id}", Collections.singletonMap("id", saveEmployee.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployee), EmployeeDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(updatedEmployee.firstName())
                .jsonPath("$.lastName").isEqualTo(updatedEmployee.lastName())
                .jsonPath("$.email").isEqualTo(updatedEmployee.email());
    }

    @DisplayName("Test find all")
    @Test
    void findAllEmployees() {
        employeeService.saveEmployee(employeeDto).block();

        EmployeeDto employeeDto2 = new EmployeeDto(
                null,
                "Le",
                "Vse",
                "z@ma.com"
        );
        employeeService.saveEmployee(employeeDto2).block();

        webTestClient.get()
                .uri("/employees")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    @DisplayName("Test get by id")
    @Test
    void getSingleEmployeeTest() {
        EmployeeDto saveEmployee = employeeService.saveEmployee(employeeDto).block();

        webTestClient.get()
                .uri("/employees/{id}", Collections.singletonMap("id", saveEmployee.id()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(saveEmployee.id())
                .jsonPath("$.firstName").isEqualTo(employeeDto.firstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.lastName())
                .jsonPath("$.email").isEqualTo(employeeDto.email());
    }

    @DisplayName("Test save")
    @Test
    void testSaveEmployee() {
        // when
        webTestClient.post().uri("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.firstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.lastName())
                .jsonPath("$.email").isEqualTo(employeeDto.email());
    }

    @DisplayName("Test delete")
    @Test
    void testDeleteEmployee() {
        EmployeeDto employeeDto1 = new EmployeeDto(
                null,
                "Lee",
                "Vsea",
                "p@ma.com"
        );
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto1).block();
        webTestClient.delete().uri("/employees/{id}", Collections.singletonMap("id", savedEmployee.id()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}
