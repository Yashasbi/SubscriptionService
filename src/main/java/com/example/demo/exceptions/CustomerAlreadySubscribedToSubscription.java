package com.example.demo.exceptions;

public class CustomerAlreadySubscribedToSubscription extends  Exception {

    public CustomerAlreadySubscribedToSubscription(String s) {
        super(s);
    }
}
