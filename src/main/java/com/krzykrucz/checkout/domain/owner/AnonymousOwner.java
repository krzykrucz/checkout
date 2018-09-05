package com.krzykrucz.checkout.domain.owner;

public class AnonymousOwner extends BasketOwner {

    @Override
    public boolean isCustomer() {
        return false;
    }
}
