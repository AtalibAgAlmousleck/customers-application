package com.atalibdev.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService customerJPADataAccessService;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        customerJPADataAccessService = new CustomerJPADataAccessService(customerRepository);
    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
    @Test
    void selectAllCustomers() {
        // When
        customerJPADataAccessService.selectAllCustomers();
        // Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        Long id = 1L;
        // When
        customerJPADataAccessService.selectCustomerById(id);
        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(
                1L, "ALY", "ali@gmail.com", 22);
        // When
        customerJPADataAccessService.insertCustomer(customer);
        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existCustomerWithEmail() {
        // Given
        String email = "developer@gmail.com";
        // When
        customerRepository.existsCustomerByEmail(email);
        // Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existCustomerWithId() {
        // Given
        Long id = 1L;
        // When
        customerJPADataAccessService.existCustomerWithId(id);
        // Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        // Given
        Long id = 1L;
        // When
        customerJPADataAccessService.deleteCustomerById(id);
        // Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(
                1L, "New Aly", "newaly@gmail.com", 20);
        // When
        customerJPADataAccessService.updateCustomer(customer);
        // Then
        verify(customerRepository).save(customer);
    }
}