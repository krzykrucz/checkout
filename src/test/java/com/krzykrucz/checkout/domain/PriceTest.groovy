package com.krzykrucz.checkout.domain

import com.krzykrucz.checkout.domain.discount.Discount
import org.joda.money.Money
import spock.lang.Specification
import spock.lang.Unroll

import static org.joda.money.CurrencyUnit.USD

class PriceTest extends Specification {

    def "should add prices"() {
        given:
        def tenDollars = Price.newDollarPrice 10
        def fiveDollars = Price.newDollarPrice 5

        expect:
        tenDollars.plus(fiveDollars) == Price.newDollarPrice(15)
    }

    @Unroll
    def "should subtract money"() {
        given:
        def tenDollars = Price.newDollarPrice 10
        def dollarsToSubtract = Money.of USD, toSubtract

        expect:
        tenDollars.minus(dollarsToSubtract).amount == expected

        where:
        toSubtract || expected
        5          || 5
        10         || 0
        15         || 0
    }

    def "should multiply prices"() {
        given:
        def tenDollars = Price.newDollarPrice 10
        expect:
        tenDollars.multiply(3) == Price.newDollarPrice(30)
    }

    def "should create dollar price"() {
        when:
        def price = Price.newDollarPrice(10)

        then:
        price.value.amount == 10
        price.value.currencyUnit == USD
    }

    def "should not create invalid dollar price"() {
        when:
        Price.newDollarPrice(-1)

        then:
        thrown IllegalArgumentException
    }

    def "should apply discount"() {
        given:
        def price = Price.newDollarPrice 20
        def discount = new Discount(Money.of(USD, 5))

        expect:
        price.apply(discount).value.amount == 15
    }

}
