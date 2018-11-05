package com.krzykrucz.checkout.application;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import spock.mock.DetachedMockFactory;

@TestConfiguration
public class APITestConfig {

    private DetachedMockFactory factory = new DetachedMockFactory();

    @Bean
    public BasketApplicationService basketApplicationService() {
        return factory.Mock(BasketApplicationService.class);
    }

}
