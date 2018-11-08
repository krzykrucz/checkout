package com.krzykrucz.checkout.domain.basket

import com.krzykrucz.checkout.domain.Price
import com.krzykrucz.checkout.domain.discount.NoneDiscountPolicy
import com.krzykrucz.checkout.domain.product.ProductId
import com.krzykrucz.checkout.domain.product.ProductInfo
import spock.lang.Specification

import static java.time.LocalDateTime.now

class BasketItemTest extends Specification {

    final static TEN_DOLLARS = Price.newDollarPrice(10)

    // TODO
    def "should increase quantity"() {
        given:
        def productInfo = new ProductInfo(
                ProductId.createNew(),
                'product',
                TEN_DOLLARS,
                now(),
                NoneDiscountPolicy.instance())

        def item = new BasketItem(productInfo, 2)

        when:
        def newItem = item.plusQuantity(3)

        then:
        newItem.quantity == 5
        newItem.productInfo == productInfo
        newItem.value == Price.newDollarPrice(50)
    }

    def "should not create basket item with invalid quantity"() {
        when: "basket item with quantity zero is created"

        then: "exception is thrown"
        // TODO
    }

}
