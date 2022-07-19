package com.example.demo.dao;

import com.example.demo.Constants;
import com.example.demo.exceptions.CustomerSubscribedToSubscription;
import com.example.demo.exceptions.SubscriptionIdInactiveException;
import com.example.demo.exceptions.SubscriptionIdNotFoundException;
import com.example.demo.mapper.CustomerSubscriptionMapper;
import com.example.demo.mapper.SubscriptionMapper;
import com.example.demo.models.Subscription;
import com.example.demo.models.output.CustomerSubscriptionOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.time.format.DateTimeFormatter;


@Repository

public class DaoClassPostgres implements Dao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SubscriptionMapper subscriptionMapper;
    
    @Autowired
    CustomerSubscriptionMapper customerSubscriptionMapper;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//    private static final Logger LOGGER = LogManager.getLogger(DaoClassPostgres.class);


    /**
     * This method is used to create subscription in the db
     * @param subscription Represents the subscription object which is to be inserted in the db.
     * @return
     * @throws Exception
     */
    @Override
    public boolean createSubscriptionInDb(Subscription subscription) throws Exception {



//        System.out.println("Date fprmatter is " +format);

        String createdAt = subscription.getCreatedTime().format(format);
        String updatedAt = subscription.getUpdatedTime().format(format);



//        System.out.println("Created at time is " +createdAt);


        String insertQuery = String.format("INSERT INTO SUBSCRIPTION_DETAILS VALUES('%s', '%s', '%s', %s, '%s', '%s');",
                subscription.getSubscriptionId(), subscription.getSubscriptionName(), subscription.getSubscriptionDetail(),
                subscription.isSubscriptionActive(), createdAt, updatedAt);

//        LOGGER.info("Insert query for inserting data in subscription table is " +insertQuery);

        jdbcTemplate.execute(insertQuery);
        return true;
    }

    /**
     * This method is used to fetch all subscriptions present in the db
     * @return List<Subscription>
     */
    @Override
    public List<Subscription> getAllSubscriptions() {

        String selectQuery = String.format("Select * from %s", Constants.SUBSCRIPTION_TABLE);
        System.out.println("Select query is " +selectQuery);
        return jdbcTemplate.query(selectQuery, subscriptionMapper);
    }

    /**
     *
     * This method is used to get all subscription details corresponding to the given subscriptionId
     * @param subscriptionId Represents the subscriptionId corresponding to which the details are to be fetched from the db.
     * @return Subscription
     * @throws SubscriptionIdNotFoundException Throws this exception if the subscriptionId is not present
     */
    @Override

    public Subscription getSubscriptionForSubscriptionId(UUID subscriptionId) throws SubscriptionIdNotFoundException {

        String selectQuery = String.format("Select * from %s where subscription_id = '%s'", Constants.SUBSCRIPTION_TABLE, subscriptionId);
        return jdbcTemplate.queryForObject(selectQuery, subscriptionMapper);
    }


    /**
     * This method is used to subscribe customer to the given list of subscriptionId
     * @param subscriptionIdList Represents the list of subscriptionId's
     * @param customerId Represents the customerId
     * @throws SubscriptionIdNotFoundException Throws this exception if the subscriptionId is not found
     * @throws SubscriptionIdInactiveException Throws this exception if the subscriptionId is inactive.
     */
    @Override
    public void subscribeCustomerToSubscription(List<UUID> subscriptionIdList, String customerId) throws SubscriptionIdNotFoundException, SubscriptionIdInactiveException {

        subscriptionIdList.stream().forEach(subscriptionId -> {
            if(!isCustomerSubscribedToSubscription(subscriptionId, customerId)) {
                String insertQuery = String.format("INSERT INTO customer_subscription_info values('%s', '%s', %s, '%s');",
                                     customerId, subscriptionId, true, LocalDateTime.now().format(format));
                jdbcTemplate.execute(insertQuery);
            }
        });

    }


    /**
     * This method is used to check whether a customer is subscribed to a particular subscriptionId
     * @param subscriptionId Represents the subscriptionId for which the subscription is to be checked
     * @param customerId Represents the customerId for which the subscription is to be checked.
     * @return true or false
     */
    public boolean isCustomerSubscribedToSubscription(UUID subscriptionId, String customerId) {

        String selectQuery = String.format("Select is_customer_subscribed from customer_subscription_info where (customer_id = '%s' and subscription_id = '%s');",customerId, subscriptionId);

        try {
            return (boolean) jdbcTemplate.queryForMap(selectQuery).getOrDefault("is_customer_subscribed", false);
        }
        catch(EmptyResultDataAccessException e) {
            return false;
        }
    }


    /**
     * This method is used to fetch all the customer details for the given subscription
     * @param subscriptionId Represents the subscriptionId for which the details is to be fetched
     * @return List<CustomerSubscriptionOutput>
     */
    public List<CustomerSubscriptionOutput> getCustomersForGivenSubscriptionId(final UUID subscriptionId) {

        String selectQuery = String.format("Select * from customer_subscription_info where subscription_id = '%s';", subscriptionId);
        List<CustomerSubscriptionOutput> customerSubscriptionList = jdbcTemplate.query(selectQuery, customerSubscriptionMapper);
        return customerSubscriptionList;
    }

    /**
     * This method is used to check if any customer is subscribed to given subscriptionId
     * @param subscriptionId Represents the subscriptionId for which the subscription is to be checked
     * @return
     */
    public boolean isCustomerSubscribedToGivenSubscriptionId(final UUID subscriptionId) {

        //Fetch the list of customers for given subscriptionId
        List<CustomerSubscriptionOutput> customerSubscriptionOutputList = getCustomersForGivenSubscriptionId(subscriptionId);

        return customerSubscriptionOutputList.stream().anyMatch(customer-> customer.isCustomerSubscribed() == true);
    }

    @Override
    public void unSubscribeCustomerToSubscription(UUID subscriptionId, String customerId) throws SubscriptionIdNotFoundException {

        String updateQuery = String.format("UPDATE customer_subscription_info SET is_customer_subscribed = false where (subscription_id = '%s' and customer_id = '%s');",
                subscriptionId, customerId);

        jdbcTemplate.execute(updateQuery);
    }


    /**
     * This method id used to mark subscription as inactive
     * @param subscriptionId Represents the subscriptionId
     * @throws CustomerSubscribedToSubscription
     * @throws SubscriptionIdNotFoundException
     */
    @Override
    public void markSubscriptionInActive(UUID subscriptionId) throws CustomerSubscribedToSubscription, SubscriptionIdNotFoundException {

           String query = String.format("UPDATE TABLE %s set is_active = '%s';", Constants.SUBSCRIPTION_TABLE, subscriptionId);
           jdbcTemplate.execute(query);
    }

    @Override
    public void activateSubscription(UUID subscriptionId) throws SubscriptionIdNotFoundException {

        System.out.println("Hello");

    }

    @Override
    public List<CustomerSubscriptionOutput> getAllCustomerSubscriptionInfo() {

        String selectQuery = "Select * from customer_subscription_info;";
        return jdbcTemplate.query(selectQuery, customerSubscriptionMapper);
    }

    /**
     * This method is used to check if the subscriptionId is present in the db or not
     * @param subscriptionId
     * @return
     */
    public boolean isSubscriptionIdPresentInDb(final UUID subscriptionId) {

        String selectQuery = String.format("Select * from %s where subscription_id = '%s';", Constants.SUBSCRIPTION_TABLE, subscriptionId);
        List<Subscription> list = jdbcTemplate.query(selectQuery, subscriptionMapper);
        return list.size() == 0? false : true;
    }
}
