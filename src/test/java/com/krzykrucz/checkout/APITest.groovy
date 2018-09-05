package com.krzykrucz.checkout

import com.krzykrucz.checkout.application.APITestConfig
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spockmvc.SpockMvcSpec

@SpringBootTest
@AutoConfigureMockMvc
@Import([APITestConfig])
class APITest extends SpockMvcSpec {

    @Override
    MockMvc buildMockMvc(WebApplicationContext webApplicationContext) {
        MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build()
    }

}
