package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor

public class CustomerSubscriptionInfo {

    String customerId;
    UUID subscriptionId;
    boolean isCustomerSubscribed;
}
