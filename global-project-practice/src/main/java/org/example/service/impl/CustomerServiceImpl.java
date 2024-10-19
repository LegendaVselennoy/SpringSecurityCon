package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Customers;
import org.example.exception.RequestValidationException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.CustomerRepository;
import org.example.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public void insertCustomer(Customers customers) {
        customerRepository.save(customers);
    }

    @Override
    public boolean existsCustomersByEmailService(String email) {
        return customerRepository.existsCustomersByEmail(email);
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        if (!customerRepository.existsCustomersById(customerId)){
            throw new ResourceNotFoundException("Customer with id [%s] not found".formatted(customerId));
        }
        customerRepository.deleteById(customerId);
    }

    @Override
    public void updateCustomer(Long customerId, Customers customer) {
        if (customerRepository.findById(customerId).isEmpty()) {
            throw new RequestValidationException("Customer with id [%s] not found".formatted(customerId));
        }
        Customers customerToUpdate = customerRepository.findById(customerId).get();
        customerToUpdate.setName(customer.getName());
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setAge(customer.getAge());
        customerRepository.save(customerToUpdate);
    }
}
