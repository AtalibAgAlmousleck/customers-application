package com.atalibdev.customer;

import com.atalibdev.AbstractTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest
        extends AbstractTestContainersUnitTest {

    private CustomerJDBCDataAccessService customerJDBCDataAccessService;
    private final CustomerRowMapper customerRowMapper =
            new CustomerRowMapper();
    @BeforeEach
    void setUp() {
        customerJDBCDataAccessService =
                new CustomerJDBCDataAccessService(
                        getJdbcTemplate(),
                        customerRowMapper
                );
    }
    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20);
        customerJDBCDataAccessService.insertCustomer(customer);
        // When
        List<Customer> customerList = customerJDBCDataAccessService
                .selectAllCustomers();
        // Then
        assertThat(customerList).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20);
        customerJDBCDataAccessService.insertCustomer(customer);

        Long id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        Optional<Customer> getCustomer = customerJDBCDataAccessService.selectCustomerById(id);

        // Then
        assertThat(getCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                });
    }
    @Test
    void returnEmptyWhenCustomerWithTheGivenIdNotFound() {
        // Given
        Long customerByIdIsNull = (long) 1;
        // When
        var actual = customerJDBCDataAccessService
                .selectCustomerById(customerByIdIsNull);
        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given


        // When

        // Then
    }

    @Test
    void existCustomerWithEmail() {
        // Given


        // When

        // Then
    }

    @Test
    void existCustomerWithId() {
        // Given


        // When

        // Then
    }

    @Test
    void deleteCustomerById() {
        // Given


        // When

        // Then
    }

    @Test
    void updateCustomer() {
        // Given


        // When

        // Then
    }
}