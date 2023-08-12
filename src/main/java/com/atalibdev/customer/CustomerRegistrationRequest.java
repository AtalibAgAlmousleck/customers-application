package com.atalibdev.customer;

public record CustomerRegistrationRequest(
        String name, String email, Integer age
) {
}
