package org.example.springguru.beer.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.springguru.beer.model.dto.CustomerDTO;
import org.example.springguru.beer.repository.CustomRepository;
import org.example.springguru.beer.service.CustomerService;
import org.example.springguru.beer.mappers.CustomerMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceIJPA implements CustomerService {

    private final CustomRepository customRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return List.of();
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        return null;
    }

    @Override
    public void updateCustomerById(UUID uuid, CustomerDTO customer) {

    }

    @Override
    public void deleteCustomerById(UUID uuid) {

    }

    @Override
    public void patchCustomerById(UUID uuid, CustomerDTO customer) {

    }
}
