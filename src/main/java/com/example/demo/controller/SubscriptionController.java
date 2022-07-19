package com.example.demo.controller;

import com.example.demo.exceptions.CustomerSubscribedToSubscription;
import com.example.demo.exceptions.SubscriptionIdInactiveException;
import com.example.demo.exceptions.SubscriptionIdNotFoundException;
import com.example.demo.models.Subscription;
import com.example.demo.models.input.CustomerSubscriptionInput;
import com.example.demo.models.output.CustomerSubscriptionOutput;
import com.example.demo.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/subscription")
@RestController

public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;


    @PostMapping
    public UUID createSubscription(@NonNull @RequestBody Subscription subscription) throws Exception {
        return subscriptionService.createSubscription(subscription);
    }

    @GetMapping("/getAllSubscriptions")
    public List<Subscription> getAllSubscriptions() {
        return subscriptionService.getAllSubscription();
    }

    @PutMapping("/markSubscriptionInactive/{subscriptionId}")
    public void updateSubscription(@PathVariable UUID subscriptionId) throws SubscriptionIdNotFoundException, CustomerSubscribedToSubscription {
//        System.out.println("SubscriptionId is " +subscriptionId);
        subscriptionService.markSubscriptionInActive(subscriptionId);
    }

    @PutMapping("/activateSubscription/{subscriptionId}")
    public void activateSubscription(@PathVariable UUID subscriptionId) throws SubscriptionIdNotFoundException, CustomerSubscribedToSubscription {
//        System.out.println("SubscriptionId is " +subscriptionId);
        subscriptionService.activateSubscription(subscriptionId);
    }

    @GetMapping("/getSubscriptionForSubscriptionId")
    public List<Subscription> getSubscriptionForSubscriptionId(@NonNull @RequestBody final ArrayList<UUID> subscriptionIdList) throws SubscriptionIdNotFoundException {
        System.out.println("Inside the function");
        System.out.println("Size of the list is " +subscriptionIdList.size());
        return subscriptionService.getSubscriptionForSubscriptionId(subscriptionIdList);
    }

    @PostMapping("/subscribe/customer")
    public void subscribeCustomerToSubscription(@RequestBody CustomerSubscriptionInput customerSubscriptionInput) throws SubscriptionIdNotFoundException, SubscriptionIdInactiveException {
        subscriptionService.subscribeCustomerToSubscription(customerSubscriptionInput.getSubscriptionIdList(), customerSubscriptionInput.getCustomerId());
    }

    @PostMapping("/unsubscribe/customer")
    public void unSubscribeCustomerToSubscription(@RequestBody CustomerSubscriptionInput customerSubscriptionInput) throws SubscriptionIdNotFoundException {
        subscriptionService.unSubscribeCustomerToSubscription(customerSubscriptionInput.getSubscriptionIdList(), customerSubscriptionInput.getCustomerId());
    }


    @GetMapping("/getCustomerSubscriptionInfo")
    public List<CustomerSubscriptionOutput> getAllCustomerSubscriptionInfo() {
        return subscriptionService.getAllCustomerSubscriptionInfo();
    }

    @GetMapping
    public String getStatus() {
        return "Service is healthy";
    }
}
