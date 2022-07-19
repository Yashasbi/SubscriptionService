package com.example.demo.models.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter

public class CustomerSubscriptionInput {

    String customerId;
    List<UUID> subscriptionIdList;
}
