package com.krzykrucz.checkout.domain.discount;

import com.krzykrucz.checkout.domain.Price;
import com.krzykrucz.checkout.domain.basket.Basket;
import com.krzykrucz.checkout.domain.basket.BasketItem;
import com.krzykrucz.checkout.domain.product.ProductId;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class QuantityDiscountPolicy implements DiscountPolicy {

    private final ProductId productId;

    private final int discountableQuantity;

    private final Price discountedProductSetPrice;

    public QuantityDiscountPolicy(ProductId productId, int discountableQuantity, Price discountedProductSetPrice) {
        checkArgument(discountableQuantity > 0);
        this.productId = checkNotNull(productId);
        this.discountableQuantity = discountableQuantity;
        this.discountedProductSetPrice = checkNotNull(discountedProductSetPrice);
    }

    @Override
    public Discount calculateDiscountFromBasket(Basket basket) {
        final Set<BasketItem> itemsInBasket = basket.getItems();
        final Optional<BasketItem> discountableBasketItem = itemsInBasket.stream()
                .filter(isDiscountApplicableToItem())
                .findFirst();
        if (!discountableBasketItem.isPresent()) {
            return Discount.zero();
        }
        final Price discountableProductPrice = discountableBasketItem.get()
                .getProductInfo()
                .getPrice();
        final Price dicountableQuantityTotalPrice = discountableProductPrice.multiply(discountableQuantity);
        return Discount.builder()
                .oldPrice(dicountableQuantityTotalPrice)
                .newPrice(discountedProductSetPrice)
                .build();
    }

    private Predicate<BasketItem> isDiscountApplicableToItem() {
        return item -> {
            final boolean isProperProduct = item.getProductInfo().getProductId()
                    .equals(this.productId);
            final boolean hasSufficientQuantity = item.getQuantity() >= discountableQuantity;
            return isProperProduct && hasSufficientQuantity;
        };
    }

    @Override
    public Optional<ProductId> applicableProduct() {
        return Optional.of(productId);
    }
}
