package com.krzykrucz.checkout.domain.product;

import com.krzykrucz.checkout.domain.Price;
import com.krzykrucz.checkout.domain.discount.DiscountPolicy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@EqualsAndHashCode(exclude = "creationTime")
@ToString
public class ProductInfo {

    private final ProductId productId;

    private final String name;

    private final Price price;

    private final LocalDateTime creationTime;

    private final DiscountPolicy availableDiscount;

    ProductInfo(ProductId productId, String name, Price price, LocalDateTime creationTime, DiscountPolicy availableDiscount) {
        this.productId = checkNotNull(productId);
        this.price = checkNotNull(price);
        this.creationTime = checkNotNull(creationTime);
        this.availableDiscount = checkNotNull(availableDiscount);
        this.name = name;
    }
}
