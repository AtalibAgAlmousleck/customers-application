package com.atalibdev.customer;

import com.atalibdev.exception.CustomerUpdateRequest;
import com.atalibdev.exception.DuplicateResourceException;
import com.atalibdev.exception.RequestValidateException;
import com.atalibdev.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void getCustomerById() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 25
        );
       when(customerDao.selectCustomerById(id))
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
        when(customerDao.selectCustomerById(id))
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
        when(customerDao.existCustomerWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Good developer always write test", email, 32);
        // When
        customerService.addCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        // Abstract the value
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void throwExceptionIfTheEmailTaking() {
        // Given
        String email = "writeTest@gmail.com";
        when(customerDao.existCustomerWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Good developer always write test", email, 32);
        // When
        assertThatThrownBy(() -> customerService.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Customer with the given email: [%s] taken"
                        .formatted(email));
        // Then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        // Given
        Long id = 1L;
        when(customerDao.existCustomerWithId(id)).thenReturn(true);

        // When
        customerService.deleteCustomerById(id);

        // Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void throwExceptionWhenCustomerIdNotFound() {
        // Given
        Long id = 1L;
        when(customerDao.existCustomerWithId(id)).thenReturn(false);

        // When
       assertThatThrownBy(() -> customerService.deleteCustomerById(id))
               .isInstanceOf(ResourceNotFoundException.class)
                       .hasMessage("Customer with id [%s] not found"
                               .formatted(id));

        // Then
        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 25);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro", newEmail, 23);

        when(customerDao.existCustomerWithEmail(newEmail)).thenReturn(false);

        // When
        customerService.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void updateCustomerName() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 25);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //String newEmail = "alexandro@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro", null, null);

        //when(customerDao.existCustomerWithEmail(newEmail)).thenReturn(false);

        // When
        customerService.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomerEmail() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 25);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail, null);

            when(customerDao.existCustomerWithEmail(newEmail)).thenReturn(false);

        // When
        customerService.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomerAge() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 25);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //String newEmail = "alexandro@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, 26);

        //when(customerDao.existCustomerWithEmail(newEmail)).thenReturn(false);

        // When
        customerService.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void throwExceptionWenUpdatingCustomerEmailWithATakingEmailAddress() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 25);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail, null);

        when(customerDao.existCustomerWithEmail(newEmail)).thenReturn(true);
        //customerService.updateCustomer(id, updateRequest);

        // When
        assertThatThrownBy(() -> customerService.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void throwExceptionWhenCustomerHasNoChanges() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 25);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge());

        // When
        //customerService.updateCustomer(id, updateRequest);
        assertThatThrownBy(() -> customerService.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidateException.class)
                .hasMessage("No data changes found");

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }
}