package com.atalibdev.customer;

import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao {

    private final CustomerRepository customerRepository;
    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }
    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customerRepository.findById(id);
    }
    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }
    @Override
    public boolean existCustomerWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }
    @Override
    public boolean existCustomerWithId(Long id) {
        return customerRepository.existsCustomerById(id);
    }
    @Override
    public void deleteCustomerById(Long customerId) {
        customerRepository.deleteById(customerId);
    }
    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
