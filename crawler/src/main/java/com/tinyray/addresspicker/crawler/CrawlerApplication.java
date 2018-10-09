package com.tinyray.addresspicker.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;



@SpringBootApplication
public class CrawlerApplication {


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CrawlerApplication.class);
        ConfigurableApplicationContext applicationContext = application.run(args);
        AddressCrawler addressCrawler = (AddressCrawler) applicationContext.getBean("crawler");
        addressCrawler.save();
        applicationContext.close();
    }
}
