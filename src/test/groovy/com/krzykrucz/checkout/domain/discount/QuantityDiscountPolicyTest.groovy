package com.krzykrucz.checkout.domain.discount

import com.krzykrucz.checkout.domain.Price
import com.krzykrucz.checkout.domain.basket.Basket
import com.krzykrucz.checkout.domain.basket.BasketItem
import com.krzykrucz.checkout.domain.product.ProductId
import com.krzykrucz.checkout.domain.product.ProductInfo
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

// TODO remove
class QuantityDiscountPolicyTest extends Specification {

    final static PRODUCT_ID_1 = ProductId.createNew()
    final static PRODUCT_ID_2 = ProductId.createNew()

    final static USD_10_DISCOUNT = new Discount(Money.of(CurrencyUnit.USD, 10))
    final static USD_5 = Price.newDollarPrice 5
    final static USD_10 = Price.newDollarPrice 10

    @Unroll
    def "should calculate discount"() {
        given:
        def basket = Stub(Basket)
        basket.getItems() >> ((Set) itemsInBasket)
        def discount = new QuantityDiscountPolicy(PRODUCT_ID_1, 3, USD_5)

        expect:
        discount.calculateDiscountFromBasket(basket) == discountValue
        discount.applicableProduct().get() == PRODUCT_ID_1

        where:
        itemsInBasket                        || discountValue
        [basketItem(PRODUCT_ID_2, USD_5, 3)] || Discount.zero()
        [basketItem(PRODUCT_ID_1, USD_5, 2),
         basketItem(PRODUCT_ID_2, USD_5, 3)] || Discount.zero()
        [basketItem(PRODUCT_ID_1, USD_5, 3),
         basketItem(PRODUCT_ID_2, USD_5, 3)] || USD_10_DISCOUNT
        [basketItem(PRODUCT_ID_1, USD_5, 5)] || USD_10_DISCOUNT

    }

    def "should not create discount with invalid discountable quantity"() {
        when:
        new QuantityDiscountPolicy(PRODUCT_ID_1, 0, USD_10)

        then:
        thrown IllegalArgumentException
    }

    def basketItem(ProductId id, Price price, int quantity) {
        def mockProduct = new ProductInfo(id, 'product', price, LocalDateTime.now(), NoneDiscountPolicy.instance())
        new BasketItem(mockProduct, quantity)
    }
}
