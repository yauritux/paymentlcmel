package com.pdd.paymentlcm.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusDto {
    @JsonProperty("customer_id")
    private String customerId;
    @JsonProperty("customer_name")
    private String customerName;
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("transaction_date")
    private String transactionDate;
    private PaymentStatus status;
}
