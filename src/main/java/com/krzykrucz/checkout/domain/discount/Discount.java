package com.krzykrucz.checkout.domain.discount;

import com.krzykrucz.checkout.domain.Price;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.joda.money.Money;

import java.util.Comparator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static lombok.AccessLevel.PRIVATE;
import static org.joda.money.CurrencyUnit.USD;

@AllArgsConstructor(access = PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class Discount {

    private static final Discount ZERO = new Discount(Money.zero(USD));

    private static final Comparator<Discount> COMPARATOR = (price1, price2) -> {
        final Money price1Value = price1.value;
        final Money price2Value = price2.value;
        checkArgument(price1Value.isSameCurrency(price2Value));

        if (price1Value.isLessThan(price2Value)) {
            return -1;
        }
        if (price1Value.isGreaterThan(price2Value)) {
            return 1;
        }
        return 0;
    };

    private final Money value;

    public static Discount zero() {
        return ZERO;
    }

    public static Comparator<Discount> comparator() {
        return COMPARATOR;
    }

    public static DiscountBuilder builder() {
        return new DiscountBuilder();
    }

    public static class DiscountBuilder {
        private Price oldPrice;
        private Price newPrice;

        private DiscountBuilder() {
        }

        public DiscountBuilder oldPrice(Price oldPrice) {
            this.oldPrice = checkNotNull(oldPrice);
            return this;
        }

        public DiscountBuilder newPrice(Price newPrice) {
            this.newPrice = checkNotNull(newPrice);
            return this;
        }

        public Discount build() {
            Money discountValue = oldPrice.minus(newPrice.getValue());
            return new Discount(discountValue);
        }
    }

}
