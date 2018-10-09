package com.tinyray.addresspicker.crawler;


import com.tinyray.addresspicker.crawler.analyzer.FirstPageAnalyzer;
import com.tinyray.addresspicker.crawler.model.Address;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class AddressCrawler{


    private String url;
    private URI firstURI;
    private String addressDataFilePath;

    public String getAddressDataFilePath() {
        return addressDataFilePath;
    }

    public void setAddressDataFilePath(String addressDataFilePath) {
        this.addressDataFilePath = addressDataFilePath;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AddressCrawler(String url) {
        try {
            this.firstURI = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private FirstPageAnalyzer firstPageAnalyzer = new FirstPageAnalyzer();


    public List<Address> crawling(){
        return firstPageAnalyzer.analyze(firstURI);
    }

    public void save(){
        File dataFile = new File(this.getAddressDataFilePath());
        if(!dataFile.getParentFile().exists()){
            dataFile.getParentFile().mkdirs();
        }
        if(!dataFile.exists()){
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String dataStr = crawling().toString();
        try {
            FileUtils.write(dataFile, dataStr, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

