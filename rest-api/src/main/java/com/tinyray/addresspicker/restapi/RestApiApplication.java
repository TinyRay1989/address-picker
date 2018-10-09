package com.tinyray.addresspicker.restapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestApiApplication {

    @Value("${spring.addressDataFile.path}")
    private String path;

    @Bean
    public AddressCache addressCache(){
        AddressCache addressCache  = new AddressCache();
        addressCache.setPath(path);
        addressCache.init();
        return addressCache;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }
}
