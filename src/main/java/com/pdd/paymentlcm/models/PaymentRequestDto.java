package com.pdd.paymentlcm.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private String serviceId;
    private String merchantId;
    private String customerId;
}
