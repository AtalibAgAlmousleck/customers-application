package com.atalibdev.customer;

import com.atalibdev.exception.CustomerUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable("id") Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @PutMapping("/{customerId}")
    public void updateCustomer(
            @PathVariable("customerId") Long customerId,
            @RequestBody CustomerUpdateRequest updateRequest ) {
        customerService.updateCustomer(customerId, updateRequest);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Long customerId) {
        customerService.deleteCustomerById(customerId);
    }
}