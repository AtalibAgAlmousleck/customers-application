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
        Long customerByIdIsNull = (long) -1;
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
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name, email, 20);
        customerJDBCDataAccessService.insertCustomer(customer);
        // When
        boolean actual = customerJDBCDataAccessService
                .existCustomerWithEmail(email);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithEmailReturnFalseWhenDoesNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // When
        boolean actual = customerJDBCDataAccessService
                .existCustomerWithEmail(email);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existCustomerWithId() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email, 20);
        customerJDBCDataAccessService.insertCustomer(customer);
        Long id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();
        // When
        var actual = customerJDBCDataAccessService
                .existCustomerWithId(id);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existCustomerByIdReturnFalseWhenIdIsNotPresent() {
        // Given
        Long id = -1L;
        // When
        var actual = customerJDBCDataAccessService
                .existCustomerWithId(id);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email, 20);
        customerJDBCDataAccessService.insertCustomer(customer);

        Long id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        customerJDBCDataAccessService.deleteCustomerById(id);
        // Then
        Optional<Customer> actual =
                customerJDBCDataAccessService.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email, 20);
        customerJDBCDataAccessService.insertCustomer(customer);
        Long id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();
        var newName = "Java Developer";
        // When
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        customerJDBCDataAccessService.updateCustomer(update);
        // Then
        Optional<Customer> actual =
                customerJDBCDataAccessService
                        .selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email, 20);
        customerJDBCDataAccessService.insertCustomer(customer);

        Long id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress()
                + "-" + UUID.randomUUID();
        // When
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        customerJDBCDataAccessService.updateCustomer(update);
        // Then
        Optional<Customer> actual =
                customerJDBCDataAccessService
                        .selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email, 20);

        customerJDBCDataAccessService.insertCustomer(customer);

        Long id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();
        var newAge = 90;
        // When
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        customerJDBCDataAccessService.updateCustomer(update);
        // Then
        Optional<Customer> actual =
                customerJDBCDataAccessService
                        .selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(newAge);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void updateAllPropertiesForCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email, 20);
        customerJDBCDataAccessService.insertCustomer(customer);

        Long id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        // When
        Customer update = new Customer();
        update.setId(id);
        update.setName("foo");
        update.setEmail(UUID.randomUUID().toString());
        update.setAge(22);

        customerJDBCDataAccessService.updateCustomer(update);

        // Then
        Optional<Customer> actual =
                customerJDBCDataAccessService.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateWhenThereIsNothingToUpdate() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email, 20);
        customerJDBCDataAccessService.insertCustomer(customer);

        Long id = customerJDBCDataAccessService
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        // When
        Customer update = new Customer();
        update.setId(id);

        customerJDBCDataAccessService.updateCustomer(update);

        // Then
        Optional<Customer> actual =
                customerJDBCDataAccessService.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }
}