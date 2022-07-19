package com.example.demo.mapper;

import com.example.demo.models.input.CustomerSubscriptionInput;
import com.example.demo.models.output.CustomerSubscriptionOutput;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class CustomerSubscriptionMapper implements RowMapper<CustomerSubscriptionOutput> {


    @Override
    public CustomerSubscriptionOutput mapRow(ResultSet resultSet, int i) throws SQLException {

        return CustomerSubscriptionOutput.builder()
                .customerId(resultSet.getString("customer_id"))
                .subscriptionId(UUID.fromString(resultSet.getString("subscription_id")))
                .isCustomerSubscribed(resultSet.getBoolean("is_customer_subscribed"))
                .subscribed_time(resultSet.getTimestamp("subscribed_time").toLocalDateTime())
                .build();

    }
}
