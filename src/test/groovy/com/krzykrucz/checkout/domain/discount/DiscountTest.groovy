package com.krzykrucz.checkout.domain.discount

import com.krzykrucz.checkout.domain.Price
import org.joda.money.Money
import spock.lang.Specification
import spock.lang.Unroll

import java.math.RoundingMode

import static org.joda.money.CurrencyUnit.USD

// TODO remove
class DiscountTest extends Specification {

    @Unroll
    def "should create discount from price change"() {
        given:
        def price1 = Price.newDollarPrice oldPrice
        def price2 = Price.newDollarPrice newPrice

        when:
        def discount = Discount.builder()
                .oldPrice(price1)
                .newPrice(price2)
                .build()

        then:
        discount.value.amount == discountValue

        where:
        oldPrice | newPrice || discountValue
        30       | 20       || 10
        20       | 30       || 0
    }

    @Unroll
    def "should compare properly"() {
        given:
        def discount1 = new Discount(Money.of(unit1, value1))
        def discount2 = new Discount(Money.of(unit2, value2, RoundingMode.UNNECESSARY))

        expect:
        Discount.comparator().compare(discount1, discount2) == comparingResult

        where:
        value1 | unit1 | value2 | unit2 || comparingResult
        0      | USD   | 1      | USD   || -1
        1      | USD   | 0      | USD   || 1
        0      | USD   | 0      | USD   || 0
    }

}
