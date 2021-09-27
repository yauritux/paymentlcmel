package com.pdd.paymentlcm.components;

import com.pdd.paymentlcm.PaymentlcmApplication;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest(classes = PaymentlcmApplication.class, properties = {"camel.file.enabled=true"})
@CamelSpringBootTest
@MockEndpoints()
public class FileHandlerRouteTest {

    @Autowired
    private ProducerTemplate template;

    @Test
    public void testCamelFileRoute() throws Exception {
        System.out.println("Sending request to append to an existing file...");
        template.sendBody("direct:appendToFile", "Hello Harmonie, arrived at " + new Date());
        System.out.println("Sent request to append to existing file...");
    }
}
