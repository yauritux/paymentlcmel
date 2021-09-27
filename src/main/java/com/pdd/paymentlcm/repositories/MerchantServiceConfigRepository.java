package com.pdd.paymentlcm.repositories;

import com.pdd.paymentlcm.models.MerchantServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantServiceConfigRepository extends JpaRepository<MerchantServiceConfig, String> {

    public MerchantServiceConfig findByServiceIdAndMerchantId(String serviceId, String merchantId);
}
