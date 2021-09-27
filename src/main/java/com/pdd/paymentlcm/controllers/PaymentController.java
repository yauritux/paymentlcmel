package com.pdd.paymentlcm.controllers;

import com.pdd.paymentlcm.models.CustomerAccountDto;
import com.pdd.paymentlcm.models.MerchantServiceConfig;
import com.pdd.paymentlcm.models.PaymentRequestDto;
import com.pdd.paymentlcm.models.PaymentStatusDto;
import com.pdd.paymentlcm.repositories.MerchantServiceConfigRepository;
import org.apache.camel.FluentProducerTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lcm/payments")
public class PaymentController {

    private final FluentProducerTemplate producer;

    public PaymentController(FluentProducerTemplate producer) {
        this.producer = producer;
    }

    @GetMapping("/{serviceId}/{merchantId}/{customerId}")
    public PaymentStatusDto getActiveConfiguration(@PathVariable String serviceId,
                                                   @PathVariable String merchantId,
                                                   @PathVariable String customerId) {
        StringBuilder body = new StringBuilder(String.format("{\"serviceId\": \"%s\", \"merchantId\": \"%s\", \"customerId\": \"%s\"}",
                serviceId, merchantId, customerId));
        return producer.withBody(body.toString()).to("direct:payment-lcm").request(PaymentStatusDto.class);
    }
}
