//package com.example.demo.dao;
//
//import com.example.demo.exceptions.CustomerSubscribedToSubscription;
//import com.example.demo.exceptions.SubscriptionIdInactiveException;
//import com.example.demo.exceptions.SubscriptionIdNotFoundException;
//import com.example.demo.models.CustomerSubscriptionInfo;
//import com.example.demo.models.Subscription;
//import com.example.demo.models.output.CustomerSubscriptionOutput;
//import org.springframework.lang.NonNull;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.UUID;
//
//@Component
//@Repository
//
//public class DaoInMemory implements Dao {
//
//
//
//    HashMap<UUID, Subscription> subscriptionDb = new HashMap<UUID, Subscription>();
//    HashMap<String, ArrayList<CustomerSubscriptionInfo>> customerSubscriptionTable = new HashMap<String, ArrayList<CustomerSubscriptionInfo>>();
//
//    /**
//     * This method is used to insert a subscription in db
//     * @param subscription Represents a subscription object to be inserted in db
//     * @return UUID Represents subscriptionId
//     */
//    public boolean createSubscriptionInDb(@NonNull Subscription subscription) throws Exception {
//
//        if(subscriptionDb.containsKey(subscription.getSubscriptionId())) {
//            throw new Exception("SubscriptionId already present in db");
//        }
//        else {
//            subscriptionDb.put(subscription.getSubscriptionId(), subscription);
//        }
//        return true;
//    }
//
//    /**
//     * This method is used to fetch all the subscription details from the db
//     */
//    public List<Subscription> getAllSubscriptions() {
//
//        List<Subscription> list = new ArrayList<>();
//
//        subscriptionDb.entrySet().stream().forEach(e->
//                list.add(e.getValue())
//        );
//
//        return list;
//    }
//    /**
//     * This method is used to get subscription details for a subscriptionId
//     * @param subscriptionId Represents the subscriptionId for which the details are to be fetched.
//     * @return Subscription
//     * @throws Exception
//     */
//     public Subscription getSubscriptionForSubscriptionId(@NonNull UUID subscriptionId) throws SubscriptionIdNotFoundException {
//        if(!subscriptionDb.containsKey(subscriptionId)) {
//            throw new SubscriptionIdNotFoundException("SubscriptionId not found exception");
//        }
//        else {
//            return subscriptionDb.get(subscriptionId);
//        }
//    }
//
//    /**
//     * This method is used to subscribe a customer to a subscriptionId
//     * @param subscriptionIdList Represents the subscriptionId for which the customer is to be subscribed.
//     * @param customerId Represents the customerId.
//     */
//    public void subscribeCustomerToSubscription(@NonNull List<UUID> subscriptionIdList, @NonNull String customerId) throws SubscriptionIdNotFoundException, SubscriptionIdInactiveException {
//
//        //Check if subscription details present in the db or not
//        for(UUID subscriptionId : subscriptionIdList) {
//            if(!subscriptionDb.containsKey(subscriptionId))
//                throw new SubscriptionIdNotFoundException("The given subscriptionId is not found in te db" +subscriptionId);
//
//            if(!subscriptionDb.get(subscriptionId).isSubscriptionActive()) {
//                throw new SubscriptionIdInactiveException("The given subscriptionId is inactive" +subscriptionId);
//            }
//        }
//
//        ArrayList<CustomerSubscriptionInfo> list = customerSubscriptionTable.getOrDefault(customerId, new ArrayList<CustomerSubscriptionInfo>());
//
//        subscriptionIdList.stream().forEach(subscriptionId -> {
//                    CustomerSubscriptionInfo customerSubscriptionInfo = CustomerSubscriptionInfo.builder()
//                            .subscriptionId(subscriptionId)
//                            .customerId(customerId)
//                            .isCustomerSubscribed(true)
//                            .build();
//
//            list.add(customerSubscriptionInfo);
//            customerSubscriptionTable.put(customerId, list);
//        });
//
//    }
//
//    /**
//     * This method is used to unsubscribe a customer to a particular subscription
//     * @param subscriptionIdList Represents the list of subscriptionId for which the customer is to be unsubscribed.
//     * @param customerId Represents the customerId
//     * @throws SubscriptionIdNotFoundException
//     * @throws CustomerSubscribedToSubscription
//     */
//    public void unSubscribeCustomerToSubscription(@NonNull List<UUID> subscriptionIdList, @NonNull String customerId) throws SubscriptionIdNotFoundException, CustomerSubscribedToSubscription {
//
//        for(int i=0; i<subscriptionIdList.size(); i++) {
//
//            UUID subscriptionId = subscriptionIdList.get(i);
//
//            if(!subscriptionDb.containsKey(subscriptionId)) {
//                throw new SubscriptionIdNotFoundException("The given subscriptionId is not found in the db");
//            }
//
//            if(!customerSubscriptionTable.containsKey(customerId)) {
//                throw new CustomerSubscribedToSubscription("There is no subscription corresponding to the given customer");
//            }
//
//            ArrayList<CustomerSubscriptionInfo> customerSubscriptionInfolist = customerSubscriptionTable.get(customerId);
//            customerSubscriptionInfolist.stream().filter(e->
//                            e.getSubscriptionId().equals(subscriptionId))
//                    .forEachOrdered(element-> element.setCustomerSubscribed(false));
//        }
//
//    }
//
//    /**
//     * This method is used to mark the given subscriptionId as inactive
//     * @param subscriptionId Represents the subscriptionId which is to be marked inactive
//     * @throws CustomerSubscribedToSubscription
//     */
//    public void markSubscriptionInActive(@NonNull UUID subscriptionId) throws CustomerSubscribedToSubscription, SubscriptionIdNotFoundException {
//        if(!subscriptionDb.containsKey(subscriptionId)) {
//            throw new SubscriptionIdNotFoundException("SubscriptionId does not exists in db");
//        }
//        if(!isCustomerSubscribedToGivenSubscription(subscriptionId)) {
//            throw new CustomerSubscribedToSubscription("For the given subscriptionId there are active customers");
//        }
//        else {
//            Subscription subscriptionDetailsForGivenId = subscriptionDb.get(subscriptionId);
//            subscriptionDetailsForGivenId.setSubscriptionActive(false);
//            subscriptionDetailsForGivenId.setUpdatedTime(LocalDateTime.now());
//        }
//    }
//
//    /**
//     * This method is used to mark the given subscriptionId as inactive
//     * @param subscriptionId Represents the subscriptionId which is to be marked inactive
//     * @throws CustomerSubscribedToSubscription
//     */
//    public void activateSubscription(@NonNull UUID subscriptionId) throws SubscriptionIdNotFoundException {
//
//        if(!subscriptionDb.containsKey(subscriptionId)) {
//            throw new SubscriptionIdNotFoundException("SubscriptionId does not exists in db");
//        }
//
//        Subscription subscriptionDetailsForGivenId = subscriptionDb.get(subscriptionId);
//        subscriptionDetailsForGivenId.setSubscriptionActive(true);
//        subscriptionDetailsForGivenId.setUpdatedTime(LocalDateTime.now());
//
//    }
//
//    @Override
//    public List<CustomerSubscriptionOutput> getAllCustomerSubscriptionInfo() {
//        return null;
//    }
//
//    /**
//     * This method is used to check if any of the customers has subscribed to given subscriptionId.
//     * @param subscriptionId Represents a subscriptionId
//     * @return true if the
//     */
//    private boolean isCustomerSubscribedToGivenSubscription(@NonNull UUID subscriptionId) {
//        return customerSubscriptionTable.entrySet().stream().allMatch(e->e.getValue().stream().allMatch(e1->
//                (e1.getSubscriptionId().equals(subscriptionId) && !e1.isCustomerSubscribed())));
//    }
//
//
//    /**
//     * This method is used to return the customer subscription information
//     * @return CustomerSubscriptionInfo
//     */
////    public ArrayList<CustomerSubscriptionInfo> getAllCustomerSubscriptionInfo() {
////
////        ArrayList<CustomerSubscriptionInfo> customerSubscriptionInfoArrayList = new ArrayList<>();
////
////        customerSubscriptionTable.entrySet().stream().forEach(listOfCustomerSubscription->
////                listOfCustomerSubscription.getValue().stream().forEach(subscriptionInfo->
////                        customerSubscriptionInfoArrayList.add(subscriptionInfo)
////                )
////        );
////
////        return customerSubscriptionInfoArrayList;
////    }
//
//
//}
