package com.krzykrucz.checkout

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spockmvc.SpockMvcSpec

@SpringBootTest
@ContextConfiguration(classes = [CheckoutApplication])
class E2ETest extends SpockMvcSpec {

    @Override
    MockMvc buildMockMvc(WebApplicationContext webApplicationContext) {
        MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build()
    }

}
