package com.pdd.paymentlcm.components;

import com.pdd.paymentlcm.models.WeatherDto;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.DefaultMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@ConditionalOnProperty(name = "camel.weather.enabled", havingValue = "true")
public class WeatherRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("rabbitmq:amq.direct?queue=weather&routingKey=weather&autoDelete=false")
                .log(LoggingLevel.ERROR, "Got this message from RabbitMQ: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, WeatherDto.class)
                .process(this::enrichWeatherDto)
                .log("After enrichment: ${body}")
                .marshal().json(JsonLibrary.Jackson, WeatherDto.class)
                .to("rabbitmq:amq.direct?queue=weather-event&routingKey=weather-event&autoDelete=false")
                .to("file:output/weather-data?fileName=weather_event.txt&fileExist=Append")
                ;
    }

    private void enrichWeatherDto(Exchange exchange) {
        WeatherDto dto = exchange.getMessage().getBody(WeatherDto.class);
        dto.setReceivedTime(new Date().toString());

        Message message = new DefaultMessage(exchange);
        message.setBody(dto);
        exchange.setMessage(message);
    }
}
