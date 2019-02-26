package com.krzykrucz.checkout.domain.basket

import com.krzykrucz.checkout.domain.Price
import com.krzykrucz.checkout.domain.discount.Discount
import com.krzykrucz.checkout.domain.product.Product
import org.joda.money.Money
import spock.lang.Specification

import static BasketState.CLOSED
import static BasketState.OPEN
import static org.joda.money.CurrencyUnit.USD

// TODO add edge cases, checking input validity or domain invariants
class BasketTest extends Specification {

    final static USD_10 = new Price(Money.of(USD, 10))
    final static USD_20 = new Price(Money.of(USD, 20))
    final static USD_21 = new Price(Money.of(USD, 21))
    final static USD_2 = new Price(Money.of(USD, 2))
    final static USD_1 = new Price(Money.of(USD, 1))

    def "should open basket"() {
        when:
        def basket = new Basket()

        then:
        basket.items == (Set) []
        basket.total == Price.zero()
        basket.basketId != null
        basket.state == OPEN
        basket.appliedDiscount == Discount.zero()
    }

    def "should add first product without discount"() {
        given:
        def basket = new Basket()
        def product = Product.newUndiscountableProduct('item', USD_10)

        when:
        basket.addItem(product, 1)

        then:
        basket.total == USD_10
        basket.items*.quantity == [1]
        basket.items*.productInfo.productId == [product.productId]
        basket.items*.productInfo.name == ['item']
        basket.state == OPEN
    }

    def "should add second time same product without discount and update it"() {
        given:
        def basket = new Basket()
        def product = Product.newUndiscountableProduct('item', USD_10)
        basket.addItem(product, 2)

        when:
        product.updatePrice USD_20
        basket.addItem(product, 2)

        then:
        basket.total.value == Money.of(USD, 80)
        basket.items*.quantity == [4]
        basket.items*.productInfo.productId == [product.productId]
        basket.state == OPEN
    }

    def "should close basket"() {
        given:
        def basket = new Basket()
        def product = Product.newUndiscountableProduct('item', USD_10)
        basket.addItem(product, 2)

        when:
        def total = basket.close()

        then:
        basket.state == CLOSED
        total == basket.total
    }


    def "should apply best discount"() {
        given:
        def basket = new Basket()
        def product1 = Product.newProductWithQuantityDiscount()
                .productName('item1')
                .productPrice(USD_20)
                .discountableQuantity(3)
                .discountedPrice(USD_1)
                .build()
        def product2 = Product.newProductWithQuantityDiscount()
                .productName('item2')
                .productPrice(USD_20)
                .discountableQuantity(2)
                .discountedPrice(USD_2)
                .build()
        def product3 = Product.newProductWithQuantityDiscount()
                .productName('item1')
                .productPrice(USD_20)
                .discountableQuantity(5)
                .discountedPrice(USD_21)
                .build()
        def product4 = Product.newUndiscountableProduct('item1', USD_20)

        when:
        basket.addItem(product1, 2)
        basket.addItem(product2, 2)
        basket.addItem(product3, 6)
        basket.addItem(product4, 6)

        then: "should apply discount to item3"
        basket.total.value == Money.of(USD, 241)
        (Set) basket.items*.quantity == (Set) [2, 2, 6, 6]
        basket.state == OPEN
    }

}
