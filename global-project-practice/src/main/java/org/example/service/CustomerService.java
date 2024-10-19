package org.example.service;

import org.example.entity.Customers;

public interface CustomerService {
    void insertCustomer(Customers customers);
    boolean existsCustomersByEmailService(String email);

    void deleteCustomerById(Long customerId);

    void updateCustomer(Long customerId, Customers customer);
}
