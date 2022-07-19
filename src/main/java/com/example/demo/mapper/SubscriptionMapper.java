package com.example.demo.mapper;

import com.example.demo.models.Subscription;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@Component
public class SubscriptionMapper implements RowMapper<Subscription> {

    @Override
    public Subscription mapRow(ResultSet resultSet, int i) throws SQLException {

        UUID subscription_id = UUID.fromString(resultSet.getString("subscription_id"));
        String subscription_name =  resultSet.getString("subscription_name");
        String subscription_details =  resultSet.getString("subscription_details");
        boolean is_active = resultSet.getBoolean("is_active");
        LocalDateTime created_time = resultSet.getTimestamp("creation_time").toLocalDateTime();
        LocalDateTime updated_time = resultSet.getTimestamp("updated_time").toLocalDateTime();


        return Subscription.builder().subscriptionId(subscription_id)
                .subscriptionName(subscription_name)
                .subscriptionDetail(subscription_details)
                .isSubscriptionActive(is_active)
                .createdTime(created_time)
                .updatedTime(updated_time)
                .build();
    }
}
