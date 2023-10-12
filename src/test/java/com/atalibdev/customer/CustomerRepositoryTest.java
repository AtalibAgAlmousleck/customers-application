package com.atalibdev.customer;

import com.atalibdev.AbstractTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainersUnitTest {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ApplicationContext applicationContext;
    @BeforeEach
    void setUp() {
        System.out.println(applicationContext.getBeanDefinitionCount());
    }
    @Test
    void existsCustomerByEmail() {
        // Given
        String email = FAKER.internet()
                .safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email, 20);
        customerRepository.save(customer);
        // When
        var actual = customerRepository
                .existsCustomerByEmail(email);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void returnNullWhenExistCustomerByEmailIsNotPresent() {
        // Given
        String email = FAKER.internet()
                .safeEmailAddress() + "-" + UUID.randomUUID();
        // When
        var actual = customerRepository.existsCustomerByEmail(email);
        // Then
        assertThat(actual).isFalse();
    }
    @Test
    void existsCustomerById() {
        // Given
        String email = FAKER.internet()
                .safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email, 20);
        customerRepository.save(customer);

        Long id = customerRepository
                .findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();
        // When
        var actual = customerRepository
                .existsCustomerById(id);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void returnNullWhenExistCustomerByIdIsNotPresent() {
        // Given
        Long id = -1L;
        // When
        var actual = customerRepository.existsCustomerById(id);
        // Then
        assertThat(actual).isFalse();

    }
}