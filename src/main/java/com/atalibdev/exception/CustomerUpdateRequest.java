package com.atalibdev.exception;

public record CustomerUpdateRequest(
        String name, String email, Integer age
) {
}
