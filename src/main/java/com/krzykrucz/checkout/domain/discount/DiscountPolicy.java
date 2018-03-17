package com.krzykrucz.checkout.domain.discount;

import com.krzykrucz.checkout.domain.basket.Basket;
import com.krzykrucz.checkout.domain.product.ProductId;

import java.util.Optional;

public interface DiscountPolicy {

    Discount calculateDiscountFromBasket(Basket basket);

    Optional<ProductId> applicableProduct();
}
