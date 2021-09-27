package com.pdd.paymentlcm.components;

import com.pdd.paymentlcm.PaymentlcmApplication;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = PaymentlcmApplication.class, properties = {"camel.seda.enabled=true"})
@CamelSpringBootTest
@MockEndpoints()
public class SedaRouteTest {

    @Autowired
    private ProducerTemplate template;

    @Test
    @DirtiesContext
    public void testMocksAreValid() {
        template.sendBody("direct:ticker", "Hello SEDA");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
