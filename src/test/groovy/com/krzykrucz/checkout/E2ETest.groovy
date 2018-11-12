package com.krzykrucz.checkout

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spockmvc.SpockMvcResult
import spockmvc.SpockMvcSpec

import static org.springframework.http.MediaType.APPLICATION_JSON

@SpringBootTest
@ContextConfiguration(classes = [CheckoutApplication])
class E2ETest extends SpockMvcSpec {

    private MockMvc mockMvc

    @Override
    MockMvc buildMockMvc(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build()
        mockMvc
    }

    SpockMvcResult postRawJson(String url, String json) {
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(APPLICATION_JSON)
                        .content(json)
        ).andReturn()

        new SpockMvcResult(result)
    }

}
