package com.pdd.paymentlcm.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDto implements Serializable {

    private String city;
    private String country;
    private double temp;
    private String unit;
    private String receivedTime;
}
