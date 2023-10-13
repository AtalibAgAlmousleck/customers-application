package com.atalibdev.customer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getInt("age")).thenReturn(20);
        Mockito.when(resultSet.getString("name")).thenReturn("Java");
        Mockito.when(resultSet.getString("email")).thenReturn("java@gmail.com");

        // When
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        // Then
        Customer exceptedCustomer = new Customer(
                1L, "Java", "java@gmail.com", 20
        );
        assertThat(actual).isEqualTo(exceptedCustomer);
    }
}