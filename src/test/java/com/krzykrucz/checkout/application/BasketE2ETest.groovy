package com.krzykrucz.checkout.application

import com.krzykrucz.checkout.E2ETest
import com.krzykrucz.checkout.domain.Price
import com.krzykrucz.checkout.domain.basket.BasketId
import com.krzykrucz.checkout.domain.basket.BasketRepository
import com.krzykrucz.checkout.domain.basket.BasketState
import com.krzykrucz.checkout.domain.product.Product
import com.krzykrucz.checkout.domain.product.ProductId
import com.krzykrucz.checkout.domain.product.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import spock.lang.Unroll

class BasketE2ETest extends E2ETest {

    @Autowired
    BasketRepository basketRepository

    @Autowired
    ProductRepository productRepository

    def "should open basket"() {
        when:
        def response = post '/basket/open'
        def id = response.json.basketId

        then:
        response.status == HttpStatus.OK
        def basketFromDb = basketRepository.findOne(BasketId.fromExisting(id))
        basketFromDb.isPresent()
        basketFromDb.get().total == Price.zero()
        basketFromDb.get().state == BasketState.OPEN
    }

    def "should add items to basket successfully"() {
        given:
        def openResponse = post '/basket/open'
        def id = openResponse.json.basketId
        def productId = sampleProductAdded()

        when:
        def request = [
                basketId : id,
                productId: productId.toString(),
                quantity : 3
        ]
        def addResponse = post '/basket/addItem', request

        then:
        addResponse.status == HttpStatus.OK
        def basketFromDb = basketRepository.findOne(BasketId.fromExisting(id))
        basketFromDb.isPresent()
        basketFromDb.get().items*.quantity == [3]
        basketFromDb.get().items*.productInfo.productId == [productId]
        basketFromDb.get().state == BasketState.OPEN
    }

    @Unroll
    def "should fail on command validation when adding item"() {
        given:
        def openResponse = post '/basket/open'
        def basketId = openResponse.json.basketId
        def productId = sampleProductAdded()

        when:
        def request = [
                basketId : hasBasketId ? basketId : null,
                productId: hasProductId ? productId.toString() : null,
                quantity : quantity
        ]
        def addResponse = post '/basket/addItem', request

        then:
        addResponse.status == HttpStatus.BAD_REQUEST
        addResponse.json.errorMessage == "Request invalid: ${message}"

        where:
        hasProductId | hasBasketId | quantity || message
        false        | true        | 3        || 'product id is not present'
        true         | false       | 3        || 'basket id is not present'
        true         | true        | null     || 'quantity is not present'
        true         | true        | -1       || 'quantity is negative'
    }

    def "should fail adding item when there is no basket"() {
        given:
        def productId = sampleProductAdded()

        when:
        def request = [
                basketId : UUID.randomUUID().toString(),
                productId: productId.toString(),
                quantity : 3
        ]
        def addResponse = post '/basket/addItem', request

        then:
        addResponse.status == HttpStatus.BAD_REQUEST
        addResponse.json.errorMessage == 'Basket not found'
    }

    def "should fail adding item of non-existent product"() {
        given:
        def openResponse = post '/basket/open'
        def basketId = openResponse.json.basketId
        def fakeProductId = UUID.randomUUID().toString()

        when:
        def request = [
                basketId : basketId,
                productId: fakeProductId,
                quantity : 3
        ]
        def addResponse = post '/basket/addItem', request

        then:
        addResponse.status == HttpStatus.BAD_REQUEST
        addResponse.json.errorMessage == "Cannot add product with id ${fakeProductId} because it doesn't exist"
    }

    def "should close basket and return total"() {
        given:
        def openResponse = post '/basket/open'
        def basketId = openResponse.json.basketId
        def productId = sampleProductAdded()

        and:
        def addRequest = [
                basketId : basketId,
                productId: productId.toString(),
                quantity : 3
        ]
        post '/basket/addItem', addRequest

        when:
        def closeRequest = [basketId: basketId]
        def closeResponse = post '/basket/close', closeRequest

        then:
        closeResponse.status == HttpStatus.OK
        closeResponse.json == [
                totalPrice: [
                        stringValue   : '0.00',
                        currencySymbol: 'USD',
                        formattedMoney: '$0.00'
                ]
        ]
        def basketFromDb = basketRepository.findOne(BasketId.fromExisting(basketId))
        basketFromDb.get().state == BasketState.CLOSED
    }

    def "should fail on closing non-existent basket"() {
        when:
        def closeRequest = [basketId: UUID.randomUUID().toString()]
        def closeResponse = post '/basket/close', closeRequest

        then:
        closeResponse.status == HttpStatus.BAD_REQUEST
        closeResponse.json.errorMessage == 'Basket not found'
    }

    def "should fail on closing non-up-to-date basket"() {
        given:
        def openResponse = post '/basket/open'
        def basketId = openResponse.json.basketId
        def product = Product.newUndiscountableProduct 'product', Price.zero()
        productRepository.save product

        and:
        def addRequest = [
                basketId : basketId,
                productId: product.productId.toString(),
                quantity : 3
        ]
        post '/basket/addItem', addRequest

        and:
        product.updatePrice(Price.newDollarPrice(10))
        productRepository.save product

        when:
        def closeRequest = [basketId: basketId]
        def closeResponse = post '/basket/close', closeRequest

        then:
        closeResponse.status == HttpStatus.BAD_REQUEST
        closeResponse.json.errorMessage ==~ 'Basket contains non-up-to-date product:.*'
    }

    def "validation should fail when closing basket"() {
        when:
        def closeRequest = [basketId: null]
        def closeResponse = post '/basket/close', closeRequest

        then:
        closeResponse.status == HttpStatus.BAD_REQUEST
        closeResponse.json.errorMessage == 'Request invalid: basket id is not present'
    }

    ProductId sampleProductAdded() {
        def product = Product.newUndiscountableProduct 'product', Price.zero()
        productRepository.save product
        product.productId
    }

}
