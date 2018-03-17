package com.krzykrucz.checkout.domain.product

import com.krzykrucz.checkout.domain.Price
import com.krzykrucz.checkout.domain.discount.NoneDiscountPolicy
import spock.lang.Specification

import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId

import static java.time.ZoneOffset.UTC

class ProductTest extends Specification {

    final static NOW = LocalDateTime.now()

    final static FIXED_CLOCK = Clock.fixed(NOW.toInstant(UTC), ZoneId.of('UTC'))

    def "should create product wit quantity discount"() {
        when:
        def product = Product.newProductWithQuantityDiscount()
                .productName('product')
                .productPrice(Price.zero())
                .discountedPrice(Price.zero())
                .discountableQuantity(2)
                .build()

        then:
        product.price == Price.zero()
        product.productId != null
        product.availableDiscount != null
    }

    def "should create product without available discounts"() {
        when:
        def product = Product.newUndiscountableProduct('product', Price.zero())

        then:
        product.price == Price.zero()
        product.productId != null
        product.availableDiscount == NoneDiscountPolicy.instance()
        product.name == 'product'
    }

    def "should generate info"() {
        given:
        def product = Product.newUndiscountableProduct('product', Price.zero())

        when:
        def info = product.generateInfo(FIXED_CLOCK)

        then:
        info.productId == product.productId
        info.price == product.price
        info.creationTime == NOW
    }

}
