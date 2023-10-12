package com.atalibdev.customer;

import com.atalibdev.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService customerService;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerDao);
    }
    @Test
    void getAllCustomers() {
        // When
        customerService.getAllCustomers();
        // Then
        Mockito.verify(customerDao).selectAllCustomers();
    }

    @Test
    void getCustomerById() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 25
        );
        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        // When
        Customer actual = customerDao.selectCustomerById(10L).orElseThrow();
        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void throwExceptionWhenCustomerWithTheGivenIdNotFound() {
        // Given
        Long id = 10L;
        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.empty());
        // When
        // Then
        assertThatThrownBy(() -> customerService.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        // Given
        Long id = 10L;
        String email = "writeTest@gmail.com";
        Mockito.when(customerDao.existCustomerWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Good developer always write test", email, 32);
        // When
        customerService.addCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class);
        Mockito.verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        // Abstract the value
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
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