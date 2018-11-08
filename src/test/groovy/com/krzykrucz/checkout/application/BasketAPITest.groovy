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
        setup: "basket service to result successfully"

        when: "request to open basket is sent"

        then: "response is successful"

    }

}
