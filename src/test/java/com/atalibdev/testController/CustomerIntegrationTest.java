package com.atalibdev.testController;

import com.atalibdev.customer.Customer;
import com.atalibdev.customer.CustomerRegistrationRequest;
import com.atalibdev.exception.CustomerUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URL = "/api/v1/customers";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@google.com";
        int age = RANDOM.nextInt(18, 99);
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(name, email, age);
        // send post request
        webTestClient.post()
                .uri(CUSTOMER_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();
        // get customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        // make sure that the customer is present
        Customer selectedCustomer = new Customer(
                name, email, age);

        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(selectedCustomer);

        //assert allCustomers != null;
        long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        selectedCustomer.setId(id);

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URL + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(selectedCustomer);
    }
    @Test
    void deleteCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@google.com";
        int age = RANDOM.nextInt(18, 99);
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(name, email, age);
        // send post request
        webTestClient.post()
                .uri(CUSTOMER_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();
        // get customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //assert allCustomers != null;
        long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        // delete customer
        webTestClient.delete()
                .uri(CUSTOMER_URL + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URL + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
    @Test
    void updateCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@google.com";
        int age = RANDOM.nextInt(18, 99);
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(name, email, age);
        // send post request
        webTestClient.post()
                .uri(CUSTOMER_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();
        // get customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        //assert allCustomers != null;
        long id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();
        // update customer
        String newName = "Developer";
        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest(
                        newName, null, null
                );
        webTestClient.put()
                .uri(CUSTOMER_URL + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_URL + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();
        // Asset
        Customer excepted = new Customer(id, newName, email, age);
        assertThat(updatedCustomer).isEqualTo(excepted);
    }
}
