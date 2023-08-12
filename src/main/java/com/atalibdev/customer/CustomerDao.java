package com.atalibdev.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long id);
    void insertCustomer(Customer customer);
    boolean existCustomerWithEmail(String email);
    boolean existCustomerWithId(Long id);
    void deleteCustomerById(Long customerId);
    void updateCustomer(Customer customer);
}
