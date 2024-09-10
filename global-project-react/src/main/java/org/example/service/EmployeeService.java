package org.example.service;

import org.example.dto.EmployeeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

    Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);

    Mono<EmployeeDto> getEmployee(Long id);

    Flux<EmployeeDto> getAllEmployees();

    Mono<EmployeeDto> updateEmployee(Long id, EmployeeDto employeeDto);

    Mono<Void> deleteEmployee(Long id);
}
