package org.example.controller;

import org.example.dto.EmployeeDto;
import org.example.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class) // Unit test
public class EmployeeControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private EmployeeService employeeService;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employeeDto = new EmployeeDto(
                1L,
                "Lege",
                "Vse",
                "r@ema.com"
        );
    }

    @DisplayName("Test method update")
    @Test
    void testUpdateEmployee() {
        given(employeeService.updateEmployee(any(Long.class),any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        WebTestClient.ResponseSpec response = webTestClient.put()
                .uri("/employees/{id}", Collections.singletonMap("id", employeeDto.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto),EmployeeDto.class)
                .exchange();

        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.firstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.lastName())
                .jsonPath("$.email").isEqualTo(employeeDto.email());
    }

    @DisplayName("Test method getId")
    @Test
    void givenEmployeeIdWhenSaveEmployeeThenReturnEmployee() {
        given(employeeService.getEmployee(employeeDto.id()))
                .willReturn(Mono.just(employeeDto));

        // when
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/employees/{id}",
                        Collections.singletonMap("id", employeeDto.id()))
                .exchange();

        // then
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.firstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.lastName())
                .jsonPath("$.email").isEqualTo(employeeDto.email());
    }

    @DisplayName("Test method findAll")
    @Test
    void givenEmployeeIdWhenFindAllEmployeeThenReturnEmployees() {
        List<EmployeeDto> list = new ArrayList<>();
        EmployeeDto employeeDto2 = new EmployeeDto(
                2L,
                "Lega",
                "Vsa",
                "r@aa.com"
        );
        list.add(employeeDto);
        list.add(employeeDto2);
        Flux<EmployeeDto> employeeDtoFlux = Flux.fromIterable(list);
        given(employeeService.getAllEmployees()).willReturn(employeeDtoFlux);

        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    @DisplayName("Test method delete")
    @Test
    public void givenEmployeeIdWhenDeleteEmployeeThenReturnEmployee() {
        Mono<Void> voidMono = Mono.empty();
        given(employeeService.deleteEmployee(employeeDto.id())).willReturn(voidMono);

        WebTestClient.ResponseSpec response = webTestClient.delete()
                .uri("/employees/{id}", Collections.singletonMap("id", employeeDto.id()))
                .exchange();

        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }

    @DisplayName("Test method post")
    @Test
    void givenEmployeeWhenSaveEmployeeThenReturnEmployee() {
        given(employeeService.saveEmployee(any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        // when
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        // then
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.firstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.lastName())
                .jsonPath("$.email").isEqualTo(employeeDto.email());
    }
}
