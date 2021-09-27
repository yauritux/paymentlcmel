package com.pdd.paymentlcm.components;

import com.pdd.paymentlcm.PaymentlcmApplication;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = PaymentlcmApplication.class, properties = {"camel.hello.enabled=true"})
@CamelSpringBootTest
@MockEndpoints()
public class HelloTest {

    @Autowired
    private ProducerTemplate template;

    @Test
    public void testMocksAreValid() {
        System.out.println("Sending 1");
        template.sendBody("direct:greeting", "Team");
        System.out.println("Sending 2");
        template.sendBody("direct:greeting", "Me");
    }
}
