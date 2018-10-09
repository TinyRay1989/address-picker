package com.tinyray.addresspicker.crawler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${spring.crawler.url}")
    private String url;

    @Value("${spring.crawler.file}")
    private String path;

    @Bean
    public AddressCrawler crawler(){
        AddressCrawler crawler = new AddressCrawler(url);
        crawler.setAddressDataFilePath(path);
        return crawler;
    }
}
