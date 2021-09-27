package com.pdd.paymentlcm.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "merchant_service_configs")
public class MerchantServiceConfig {

    @Id
    private String id;
    @Column(name = "merchant_id")
    private String merchantId;
    @Column(name = "merchant_name")
    private String merchantName;
    @Column(name = "service_id")
    private String serviceId;
    @Column(name = "service_name")
    private String serviceName;
    @Column(name = "service_endpoint")
    private String serviceEndpoint;
    @Column(name = "product_type")
    private String productType;
    @Column(name = "instrument_type")
    private String instrumentType;
    @Column(name = "countries")
    private String countries;
}
