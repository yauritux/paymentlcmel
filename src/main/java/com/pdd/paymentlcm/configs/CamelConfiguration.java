package com.pdd.paymentlcm.configs;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfiguration {

    public static final String RABBIT_URI = "rabbitmq:amq.direct?queue=%s&routingKey=%s&autoDelete=false";

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }
}
