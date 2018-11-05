package com.krzykrucz.checkout.application

import com.krzykrucz.checkout.APITest
import com.krzykrucz.checkout.domain.basket.BasketId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class BasketAPITest extends APITest {

    final static SAMPLE_BASKET_ID = BasketId.createNew()

    @Autowired
    BasketApplicationService basketApplicationService


    def "should open basket"() {
        setup:
        1 * basketApplicationService.openBasket() >> new OpenBasketResult(SAMPLE_BASKET_ID)

        when:
        def response = post '/basket/open'
        def id = response.json.basketId

        then:
        response.status == HttpStatus.OK
        id == SAMPLE_BASKET_ID.toString()

    }

}
