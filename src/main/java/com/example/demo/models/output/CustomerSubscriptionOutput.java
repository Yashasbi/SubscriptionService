package com.example.demo.models.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter

public class CustomerSubscriptionOutput {

    String customerId;

    UUID subscriptionId;

    boolean isCustomerSubscribed;

    LocalDateTime subscribed_time;

}
