package com.atalibdev.customer;

import com.atalibdev.exception.CustomerUpdateRequest;
import com.atalibdev.exception.DuplicateResourceException;
import com.atalibdev.exception.RequestValidateException;
import com.atalibdev.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Long id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer with id [%s] not found"
                                .formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest request) {
        // check if email taken
        String email = request.email();
        if (customerDao.existCustomerWithEmail(email)) {
            throw new DuplicateResourceException(
                    "Customer with the given email: [%s] taken"
                            .formatted(email)
            );
        }
        Customer customer = new Customer(
                request.name(),
                request.email(),
                request.age());
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomerById(Long customerId) {
        if (!customerDao.existCustomerWithId(customerId)) {
            throw new ResourceNotFoundException("Customer with id [%s] not found"
                    .formatted(customerId));
        }
        customerDao.deleteCustomerById(customerId);
    }

    public void updateCustomer(Long customerId,
                               CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomerById(customerId);
        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existCustomerWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException("email already taken");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidateException("No data changes found");
        }

        customerDao.updateCustomer(customer);
    }
}
