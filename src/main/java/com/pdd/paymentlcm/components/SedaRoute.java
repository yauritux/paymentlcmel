package com.pdd.paymentlcm.components;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.DefaultMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(name = "camel.seda.enabled", havingValue = "true")
public class SedaRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:ping?period=500")
                .process(exchange -> {
                    Message message = new DefaultMessage(exchange);
                    message.setBody(new Date());
                    exchange.setMessage(message);
                })
                .to("seda:weightShifter");

        from("seda:weightShifter?multipleConsumers=true")
                .to("direct:complexProcess");

        from("direct:complexProcess")
                .log("${body}")
                .process(exchange -> TimeUnit.SECONDS.sleep(2))
                .end();
    }
}
