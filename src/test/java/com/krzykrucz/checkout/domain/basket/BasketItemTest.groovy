package com.krzykrucz.checkout.domain.basket

import com.krzykrucz.checkout.domain.Price
import com.krzykrucz.checkout.domain.product.ProductInfo
import spock.lang.Specification

class BasketItemTest extends Specification {

    final static TEN_DOLLARS = Price.newDollarPrice(10)

    def "should increase quantity"() {
        given:
        def productInfo = GroovyMock(ProductInfo)
        productInfo.price >> TEN_DOLLARS
        def item = new BasketItem(productInfo, 2)

        when:
        def newItem = item.plusQuantity(3)

        then:
        newItem.quantity == 5
        newItem.productInfo == productInfo
        newItem.value == Price.newDollarPrice(50)
    }

    def "should not create basket item with invalid quantity"() {
        when :
        new BasketItem(GroovyMock(ProductInfo), 0)

        then:
        thrown IllegalArgumentException
    }

}
