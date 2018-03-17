package com.krzykrucz.checkout.domain.discount;

import com.krzykrucz.checkout.domain.basket.Basket;
import com.krzykrucz.checkout.domain.product.ProductId;

import java.util.Optional;

public final class NoneDiscountPolicy implements DiscountPolicy {

    private static final NoneDiscountPolicy INSTANCE = new NoneDiscountPolicy();

    private NoneDiscountPolicy() {
    }

    public static NoneDiscountPolicy instance() {
        return INSTANCE;
    }

    @Override
    public Discount calculateDiscountFromBasket(Basket basket) {
        return Discount.zero();
    }

    @Override
    public Optional<ProductId> applicableProduct() {
        return Optional.empty();
    }
}
