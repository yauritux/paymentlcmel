package com.pdd.paymentlcm.components;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "camel.file.enabled", havingValue = "true")
public class FileHandlerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        System.out.println("In file...");
        // copy one file to another folder
        from("file:input/country-data?fileName=countries.txt&noop=true")
                .to("file:output/country-data");

        // append data to an existing file
        from("direct:appendToFile")
                .process(Exchange::getMessage)
                .to("file:output/person-data?fileName=persons.txt&fileExist=Append");
    }
}
