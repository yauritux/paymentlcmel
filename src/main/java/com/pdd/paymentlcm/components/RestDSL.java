package com.pdd.paymentlcm.components;

import com.pdd.paymentlcm.models.WeatherDto;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.DefaultMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "camel.restdsl.enabled", havingValue = "true")
public class RestDSL extends RouteBuilder {

    private final WeatherDataProvider weatherDataProvider;

    public RestDSL(WeatherDataProvider weatherDataProvider) {
        this.weatherDataProvider = weatherDataProvider;
    }

    // http://localhost:8080/javadsl/weather/{city}
    @Override
    public void configure() throws Exception {
        restConfiguration().host("localhost").port("8080");

        from("rest:get:javadsl/weather/{city}?produces=application/json")
                .outputType(WeatherDto.class)
                .process(this::getWeatherData);
    }

    private void getWeatherData(Exchange exchange) {
        String city = exchange.getMessage().getHeader("city", String.class);
        var currentWeather = weatherDataProvider.getCurrentWeather(city);
        Message message = new DefaultMessage(exchange.getContext());
        message.setBody(currentWeather);
        exchange.setMessage(message);
    }
}
