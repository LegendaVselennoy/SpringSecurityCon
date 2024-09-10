package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.EmployeeDto;
import org.example.entity.Employee;
import org.example.mapper.EmployeeMapper;
import org.example.repository.EmployeeRepository;
import org.example.service.EmployeeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        Flux<Employee> employeesFlux = employeeRepository.findAll();
        return employeesFlux
                .map(EmployeeMapper::mapToEmployeeDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        return savedEmployee
                .map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(Long id, EmployeeDto employeeDto) {
        Mono<Employee> employeeMono = employeeRepository.findById(id);

        Mono<Employee> updatedEmployeeMono = employeeMono.flatMap((existingEmployee) -> {
            existingEmployee.setFirstName(employeeDto.firstName());
            existingEmployee.setLastName(employeeDto.lastName());
            existingEmployee.setEmail(employeeDto.email());

            return employeeRepository.save(existingEmployee);
        });
        return updatedEmployeeMono
                .map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<EmployeeDto> getEmployee(Long id) {
        Mono<Employee> savedEmployee = employeeRepository.findById(id);
        return savedEmployee
                .map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<Void> deleteEmployee(Long id) {
        return employeeRepository.deleteById(id);
    }
}
