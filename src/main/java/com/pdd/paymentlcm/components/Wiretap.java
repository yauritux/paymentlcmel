package com.pdd.paymentlcm.components;

import com.pdd.paymentlcm.models.TransactionDto;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.DefaultMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@ConditionalOnProperty(name = "camel.wiretap.enabled", havingValue = "true")
public class Wiretap extends RouteBuilder {

    public static final String SENDER = "sender";
    public static final String RECEIVER = "receiver";
    public static final String AUDIT_TRANSACTION_ROUTE = "direct:audit-transaction";
    public static final String AUDIT = "audit-transactions";
    static final String RABBIT_URI = "rabbitmq://localhost:5671/amq.direct?queue=%s&routingKey=%s&autoDelete=false";

    @Override
    public void configure() throws Exception {
        fromF(RABBIT_URI, SENDER, SENDER)
                .unmarshal().json(JsonLibrary.Jackson, TransactionDto.class)
                .wireTap(AUDIT_TRANSACTION_ROUTE)
                .process(this::enrichTransactionDto)
                .marshal().json(JsonLibrary.Jackson, TransactionDto.class)
                .toF(RABBIT_URI, RECEIVER, RECEIVER)
                .log("Money transferred: ${body}");

        from(AUDIT_TRANSACTION_ROUTE)
                .process(this::enrichTransactionDto)
                .marshal().json(JsonLibrary.Jackson, TransactionDto.class)
                .toF(RABBIT_URI, AUDIT, AUDIT);
    }

    private void enrichTransactionDto(Exchange exchange) {
        TransactionDto dto = exchange.getMessage().getBody(TransactionDto.class);
        dto.setTransactionDate(new Date().toString());

        Message message = new DefaultMessage(exchange);
        message.setBody(dto);
        exchange.setMessage(message);
    }
}
