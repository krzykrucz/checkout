package com.krzykrucz.checkout.domain.owner;

public class Customer extends BasketOwner {

    @Override
    public boolean isCustomer() {
        return true;
    }
}
