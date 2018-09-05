package com.krzykrucz.checkout.domain.basket;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.krzykrucz.checkout.domain.owner.AnonymousOwner;
import com.krzykrucz.checkout.domain.owner.BasketOwner;
import com.krzykrucz.checkout.domain.Price;
import com.krzykrucz.checkout.domain.discount.Discount;
import com.krzykrucz.checkout.domain.owner.Customer;
import com.krzykrucz.checkout.domain.product.Product;
import com.krzykrucz.checkout.domain.product.ProductId;
import com.krzykrucz.checkout.domain.product.ProductInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Clock;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

@Getter
@EqualsAndHashCode(of = "basketId")
public class Basket {

    private static final Clock LOCAL_TIME_CLOCK = Clock.systemDefaultZone();

    private final BasketId basketId;

    private BasketOwner basketOwner;

    private final Map<ProductId, BasketItem> items;

    private Price total;

    private BasketState state;

    private Discount appliedDiscount;

    public Basket(BasketOwner details) {
        basketId = BasketId.createNew();
        total = Price.zero();
        items = Maps.newHashMap();
        state = BasketState.OPEN;
        appliedDiscount = Discount.zero();
        basketOwner = details;
    }

    public Basket() {
        this(new AnonymousOwner());
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
        checkState(basketOwner.isCustomer(), "Non-customer cannot close a basket");
        this.state = BasketState.CLOSED;
        return total;
    }

    public void unanonymize(Customer customer) {
        checkState(!basketOwner.isCustomer(), "Able to unanonymize a basket with an anonymous owner only");
        this.basketOwner = customer;
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
