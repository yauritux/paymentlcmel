package com.pdd.paymentlcm.components;

import com.pdd.paymentlcm.models.*;
import com.pdd.paymentlcm.repositories.MerchantServiceConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.DefaultMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class PaymentRoute extends RouteBuilder {

    private final MerchantServiceConfigRepository repository;

    public PaymentRoute(MerchantServiceConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    public void configure() throws Exception {
        from("direct:payment-lcm")
                .log("receiving object: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, PaymentRequestDto.class)
                .process(this::processingWorkflow)
                .log("payment for customer account ${body} is processed.")
                .multicast().parallelProcessing()
                .to("direct:lulupay", "direct:invoices")
                .choice()
                .when(header("status").isEqualTo(false))
                        .to("direct:paymentRejected")
                    .otherwise()
                        .to("direct:paymentAccepted")
                    .end()
                .end()
                ;

        from("direct:lulupay")
                .log("${body}")
                        .end();

        from("direct:invoices")
                .log("${body}")
                        .end();

        from("direct:paymentRejected")
                .process(this::rejectPayment)
                .wireTap("direct:payment-audit")
                .log("payment is rejected due to unsufficient balance");

        from("direct:paymentAccepted")
                .process(this::acceptPayment)
                .wireTap("direct:payment-audit")
                .log("Payment successfully processed");

        from("direct:payment-audit")
                .marshal().json(JsonLibrary.Jackson, PaymentStatusDto.class)
                .choice()
                .when(header("status").isEqualTo(false))
                .to("rabbitmq:amq.direct?queue=payment-failed&routingKey=payment-failed&autoDelete=false")
                .otherwise()
                .to("rabbitmq:amq.direct?queue=payment-success&routingKey=payment-success&autoDelete=false")
                .end();
    }

    private void acceptPayment(Exchange exchange) {
        CustomerAccountDto dto = exchange.getMessage().getBody(CustomerAccountDto.class);
        PaymentStatusDto paymentStatus = PaymentStatusDto.builder()
                .transactionId(UUID.randomUUID().toString())
                .transactionDate(new Date().toString())
                .customerId(dto.getCustomerId())
                .customerName(dto.getCustomerName())
                .status(PaymentStatus.ACCEPTED)
                .build();
        log.info("payment status = {}", paymentStatus);
        Message message = new DefaultMessage(exchange.getContext());
        message.setHeader("status", true);
        message.setBody(paymentStatus);
        exchange.setMessage(message);
    }

    private void rejectPayment(Exchange exchange) {
        CustomerAccountDto dto = exchange.getMessage().getBody(CustomerAccountDto.class);
        PaymentStatusDto paymentStatus = PaymentStatusDto.builder()
                .transactionId(UUID.randomUUID().toString())
                .transactionDate(new Date().toString())
                .customerId(dto.getCustomerId())
                .customerName(dto.getCustomerName())
                .status(PaymentStatus.REJECTED)
                .build();
        log.info("payment status = {}", paymentStatus);
        Message message = new DefaultMessage(exchange.getContext());
        message.setHeader("status", false);
        message.setBody(paymentStatus);
        exchange.setMessage(message);
    }

    private void processingWorkflow(Exchange exchange) {
        PaymentRequestDto dto = exchange.getMessage().getBody(PaymentRequestDto.class);
        MerchantServiceConfig currentConfig = repository.findByServiceIdAndMerchantId(dto.getServiceId(), dto.getMerchantId());
        log.info("currentConfig = {}", currentConfig);
        String customerEndpoint = currentConfig.getServiceEndpoint().replace("{id}", dto.getCustomerId());
        log.info("calling next endpoint {}", customerEndpoint);
        RestTemplate restClient = new RestTemplate();
        ResponseEntity<CustomerAccountDto> customer = restClient.getForEntity(customerEndpoint, CustomerAccountDto.class);
        Message message = new DefaultMessage(exchange.getContext());
        message.setHeader("status", customer.getBody().isActive());
        message.setBody(customer.getBody());
        exchange.setMessage(message);
    }
}