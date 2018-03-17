package com.krzykrucz.checkout.domain.basket;

import com.krzykrucz.checkout.domain.Price;
import com.krzykrucz.checkout.domain.product.ProductInfo;
import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
public class BasketItem {
    private final ProductInfo productInfo;
    private final int quantity;

    public BasketItem(ProductInfo productInfo, int quantity) {
        checkArgument(quantity > 0);
        this.productInfo = productInfo;
        this.quantity = quantity;
    }

    public BasketItem plusQuantity(int quantity) {
        checkArgument(quantity >= 0);
        return new BasketItem(this.productInfo, this.quantity + quantity);
    }

    public Price getValue() {
        return productInfo.getPrice()
                .multiply(quantity);
    }
}
