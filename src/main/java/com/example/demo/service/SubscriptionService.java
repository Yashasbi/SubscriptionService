package com.example.demo.service;

import com.example.demo.dao.DaoClassPostgres;
import com.example.demo.exceptions.CustomerSubscribedToSubscription;
import com.example.demo.exceptions.SubscriptionIdInactiveException;
import com.example.demo.exceptions.SubscriptionIdNotFoundException;
import com.example.demo.models.Subscription;
import com.example.demo.models.output.CustomerSubscriptionOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {

    @Autowired
    DaoClassPostgres daoInMemory;
    /**
     * This method is used to create a subscription
     * @param subscription Represents the name of subscription
     */
    public UUID createSubscription(@NonNull Subscription subscription) throws Exception {

        //Create a subscriptionId
        UUID subscriptionId = UUID.randomUUID();

        subscription.setSubscriptionId(subscriptionId);
        subscription.setCreatedTime(LocalDateTime.now());
        subscription.setUpdatedTime(LocalDateTime.now());

        daoInMemory.createSubscriptionInDb(subscription);
        return subscriptionId;
    }

    /**
     * This method is used to getAll Subscriptions from a db
     * @return List<Subscription>
     */
    public List<Subscription> getAllSubscription() {
        return daoInMemory.getAllSubscriptions();
    }

    /**
     * This method is used to mark subscription as inactive
     * @param subscriptionId Represents the subscriptionId which is to be marked as inactive
     * @throws CustomerSubscribedToSubscription
     * @throws SubscriptionIdNotFoundException
     */
    public void markSubscriptionInActive(@NonNull UUID subscriptionId) throws CustomerSubscribedToSubscription, SubscriptionIdNotFoundException {


        //1.Check if subscriptionId is present in db or not
        if(!daoInMemory.isSubscriptionIdPresentInDb(subscriptionId)) {
             throw new SubscriptionIdNotFoundException("The given subscriptionId is not present in db");
         }

        //2.Check if customer is subscribed to subscriptionId
         if(daoInMemory.isCustomerSubscribedToGivenSubscriptionId(subscriptionId)) {
             throw new CustomerSubscribedToSubscription("Customer is already subscribed to subscription");
         }

         daoInMemory.markSubscriptionInActive(subscriptionId);
    }


    /**
     * This method is used to activate subscription
     * @param subscriptionId Represents the subscriptionId which is to be marked as inactive
     * @throws CustomerSubscribedToSubscription
     * @throws SubscriptionIdNotFoundException
     */
    public void activateSubscription(@NonNull UUID subscriptionId) throws CustomerSubscribedToSubscription, SubscriptionIdNotFoundException {
        daoInMemory.activateSubscription(subscriptionId);
    }

    /**
     * THis method is used to fetch all the subscription details for the given subscriptionId
     * @param subscriptionIdList Represents the list of subscriptionId for which the details are to be fetched
     */
    public List<Subscription> getSubscriptionForSubscriptionId(@RequestBody @NonNull  ArrayList<UUID> subscriptionIdList) throws SubscriptionIdNotFoundException {

        ArrayList<Subscription> subsList = new ArrayList<Subscription>();

        for (UUID subscriptionId : subscriptionIdList) {
            Subscription subscription = daoInMemory.getSubscriptionForSubscriptionId(subscriptionId);
            System.out.println("Subscription object is " +subscription);
            subsList.add(subscription);
        }

        return subsList;
    }

    /**
     * This method is used to subscribe a customer to a list of subscriptions
     * @param subscriptionList List of subscription to which the customer has to subscribe
     * @param customerId Represents the customerId
     */
    public void subscribeCustomerToSubscription(@NonNull List<UUID> subscriptionList, @NonNull String customerId) throws SubscriptionIdNotFoundException, SubscriptionIdInactiveException {
        daoInMemory.subscribeCustomerToSubscription(subscriptionList, customerId);
    }

    /**
     * This method is used to unsubscribe a customer to a list of subscriptions
     * @param subscriptionList List of subscription to which the customer has to subscribe
     * @param customerId Represents the customerId
     */
    public void unSubscribeCustomerToSubscription(@NonNull List<UUID> subscriptionList, @NonNull String customerId) throws SubscriptionIdNotFoundException {

        for(UUID subscriptionId : subscriptionList) {

            //Check if subscriptionId is present in db or not
            if(!daoInMemory.isSubscriptionIdPresentInDb(subscriptionId)) {
                throw new SubscriptionIdNotFoundException("SubscriptionId is not present in db");
            }

            daoInMemory.unSubscribeCustomerToSubscription(subscriptionId, customerId);
        }
    }

    /**
     * This method is used to return the customer subscription information from the db
     * @return ArrayList<CustomerSubscriptionInfo>
     */
    public List<CustomerSubscriptionOutput> getAllCustomerSubscriptionInfo() {
        return daoInMemory.getAllCustomerSubscriptionInfo();
    }


}
