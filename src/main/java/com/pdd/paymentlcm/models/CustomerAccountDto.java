package com.pdd.paymentlcm.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountDto implements Serializable {

    @JsonProperty("customer_id")
    private String customerId;
    @JsonProperty("customer_name")
    private String customerName;
    @JsonProperty("current_balance")
    private String currentBalance;
    private String currency;
    private boolean active;
}
