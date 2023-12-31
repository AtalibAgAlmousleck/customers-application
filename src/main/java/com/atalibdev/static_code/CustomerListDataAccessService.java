package com.atalibdev.static_code;

import com.atalibdev.customer.Customer;
import com.atalibdev.customer.CustomerDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
    // db
    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(1L, "Alex", "alex@gmail.com", 28);
        customers.add(alex);
        Customer jamila = new Customer(2L, "Jamila", "jamila@gmail.com", 24);
        customers.add(jamila);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
         return customers
                 .stream().filter(c -> c.getId().equals(id))
                 .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existCustomerWithEmail(String email) {
        return customers.stream()
                .anyMatch(customer -> customer.getEmail()
                        .equals(email));
    }

    @Override
    public boolean existCustomerWithId(Long id) {
        return customers.stream().anyMatch(customer -> customer.getId()
                .equals(id));
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        customers.stream()
                .filter(customer -> customer.getId().equals(customerId))
                .findFirst().ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }

}
