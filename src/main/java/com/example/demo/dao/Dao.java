package com.example.demo.dao;

import com.example.demo.exceptions.CustomerSubscribedToSubscription;
import com.example.demo.exceptions.SubscriptionIdInactiveException;
import com.example.demo.exceptions.SubscriptionIdNotFoundException;
import com.example.demo.models.Subscription;
import com.example.demo.models.output.CustomerSubscriptionOutput;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface Dao {

    public boolean createSubscriptionInDb(@NonNull Subscription subscription) throws Exception;

    public List<Subscription> getAllSubscriptions();

    public Subscription getSubscriptionForSubscriptionId(@NonNull UUID subscriptionId) throws SubscriptionIdNotFoundException;

    public void subscribeCustomerToSubscription(@NonNull List<UUID> subscriptionIdList, @NonNull String customerId) throws SubscriptionIdNotFoundException, SubscriptionIdInactiveException;

    public void unSubscribeCustomerToSubscription(@NonNull UUID subscriptionIdList, @NonNull String customerId) throws SubscriptionIdNotFoundException, CustomerSubscribedToSubscription;

    public void markSubscriptionInActive(@NonNull UUID subscriptionId) throws CustomerSubscribedToSubscription, SubscriptionIdNotFoundException;

    public void activateSubscription(@NonNull UUID subscriptionId) throws SubscriptionIdNotFoundException;

    public List<CustomerSubscriptionOutput> getAllCustomerSubscriptionInfo();


}
