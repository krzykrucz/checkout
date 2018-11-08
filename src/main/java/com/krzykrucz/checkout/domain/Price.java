package com.krzykrucz.checkout.domain;

import com.krzykrucz.checkout.domain.discount.Discount;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.joda.money.Money;

import static com.google.common.base.Preconditions.checkArgument;
import static org.joda.money.CurrencyUnit.USD;

@EqualsAndHashCode
@ToString
public class Price {

    private static final Price ZERO = new Price(Money.zero(USD));

    @Getter
    private final Money value;

    public Price(Money value) {
        this.value = value;
    }

    public static Price zero() {
        return ZERO;
    }

    public static Price newDollarPrice(double monetaryValue) {
        checkArgument(monetaryValue >= 0);
        return new Price(Money.of(USD, monetaryValue));
    }

    public Price plus(Price anotherPrice) {
        checkArgument(this.value.isSameCurrency(anotherPrice.value));
        return new Price(this.value.plus(anotherPrice.value));
    }

    public Money minus(Money moneyToSubtract) {
        final Money oldPriceValue = this.value;
        if (moneyToSubtract.isGreaterThan(oldPriceValue)) {
            return Money.zero(oldPriceValue.getCurrencyUnit());
        }
        return oldPriceValue.minus(moneyToSubtract);
    }

    public Price multiply(int multiplier) {
        checkArgument(multiplier >= 0);
        return new Price(this.value.multipliedBy(multiplier));
    }

    public Price apply(Discount discount) {
        final Money discountedValue = this.minus(discount.getValue());
        return new Price(discountedValue);
    }
}
