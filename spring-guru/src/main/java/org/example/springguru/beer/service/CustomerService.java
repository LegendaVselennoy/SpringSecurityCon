package org.example.springguru.beer.service;

import org.example.springguru.beer.model.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Optional<CustomerDTO> getCustomerById(UUID uuid);

    List<CustomerDTO> getAllCustomers();

    CustomerDTO saveNewCustomer(CustomerDTO customer);

    void updateCustomerById(UUID uuid, CustomerDTO customer);

    void deleteCustomerById(UUID uuid);

    void patchCustomerById(UUID uuid, CustomerDTO customer);
}
