package com.krzykrucz.checkout.domain.basket;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.krzykrucz.checkout.domain.Price;
import com.krzykrucz.checkout.domain.discount.Discount;
import com.krzykrucz.checkout.domain.product.Product;
import com.krzykrucz.checkout.domain.product.ProductId;
import com.krzykrucz.checkout.domain.product.ProductInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Clock;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = "basketId")
public class Basket {

    private static final Clock LOCAL_TIME_CLOCK = Clock.systemDefaultZone();

    private final BasketId basketId;

    private final Map<ProductId, BasketItem> items;

    private Price total;

    private BasketState state;

    private Discount appliedDiscount;

    public Basket() {
        basketId = BasketId.createNew();
        total = Price.zero();
        items = Maps.newHashMap();
        state = BasketState.OPEN;
        appliedDiscount = Discount.zero();
    }

    public void addItem(Product product, int quantity) {
        final ProductId productId = product.getProductId();
        final BasketItem basketItem;
        if (items.containsKey(productId)) {
            final BasketItem existingBasketItem = this.items.get(productId);
            final int newItemQuantity = existingBasketItem.getQuantity() + quantity;
            basketItem = new BasketItem(product.generateInfo(LOCAL_TIME_CLOCK), newItemQuantity);
        } else {
            basketItem = new BasketItem(product.generateInfo(LOCAL_TIME_CLOCK), quantity);
        }
        this.items.put(productId, basketItem);
        this.calculateTotalPrice();
    }

    public Price close() {
        this.state = BasketState.CLOSED;
        return total;
    }

    public Set<BasketItem> getItems() {
        return ImmutableSet.copyOf(items.values());
    }

    private void calculateTotalPrice() {
        this.total = this.items.values()
                .stream()
                .map(BasketItem::getValue)
                .reduce(Price.zero(), Price::plus);
        this.applyBiggestDiscount();
    }

    private void applyBiggestDiscount() {
        final Optional<Discount> biggestDiscountAvailable = this.items.values().stream()
                .map(BasketItem::getProductInfo)
                .map(ProductInfo::getAvailableDiscount)
                .filter(discountPolicy -> discountPolicy.applicableProduct().isPresent())
                .map(discountPolicy -> discountPolicy.calculateDiscountFromBasket(this))
                .max(Discount.comparator());
        biggestDiscountAvailable.ifPresent(discount -> {
            this.total = this.total.apply(discount);
            this.appliedDiscount = discount;
        });
    }
}
