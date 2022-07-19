package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor

public class Subscription {

    UUID subscriptionId;

    String subscriptionName;

    String subscriptionDetail;

    boolean isSubscriptionActive;

    LocalDateTime createdTime;

    LocalDateTime updatedTime;

}
