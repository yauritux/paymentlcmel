package com.pdd.paymentlcm.components;

import com.pdd.paymentlcm.models.WeatherDto;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class WeatherDataProvider {

    private static Map<String, WeatherDto> weatherData = new HashMap<>();

    public WeatherDataProvider() {
        WeatherDto dto = WeatherDto.builder().city("London").temp(10).unit("celcius").receivedTime(new Date().toString()).build();
        weatherData.put("LONDON", dto);
    }

    public WeatherDto getCurrentWeather(String city) {
        return weatherData.get(city.toUpperCase());
    }

    public void setCurrentWeather(WeatherDto dto) {
        dto.setReceivedTime(new Date().toString());
        weatherData.put(dto.getCity().toUpperCase(), dto);
    }
}
