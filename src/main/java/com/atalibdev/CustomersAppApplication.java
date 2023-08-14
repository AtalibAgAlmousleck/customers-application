package com.atalibdev;

import com.atalibdev.customer.Customer;
import com.atalibdev.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CustomersAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomersAppApplication.class, args);
    }

    //@Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        Customer alex = new Customer("Alex", "alex@google.com", 24);
        Customer jamila = new Customer("Jamila", "jamila@finance.com", 19);
        Customer nelson = new Customer("Nelson", "nelson@code.com", 25);
        return args -> {
            customerRepository.saveAll(
                    List.of(alex, jamila, nelson)
            );
        };
    }
}
