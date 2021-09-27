package com.pdd.paymentlcm.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TransactionDto implements Serializable {

    private String transactionId;
    private String senderAccountId;
    private String receiverAccountId;
    private double amount;
    private String currency;
    private String transactionDate;
}
