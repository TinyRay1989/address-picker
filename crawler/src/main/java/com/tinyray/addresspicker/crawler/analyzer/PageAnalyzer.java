package com.tinyray.addresspicker.crawler.analyzer;

import com.tinyray.addresspicker.crawler.model.Address;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public abstract class PageAnalyzer {

    private String css = "provincetr";

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    protected Document connectAndGetDocument(String urlStr) {
        if(urlStr == null || urlStr.isEmpty()){
            throw new IllegalArgumentException("the input url : " + urlStr + " is invalid");
        }
        URL url = null;
        try {
            url = new URL(urlStr);
            //url.openConnection().setReadTimeout(I);
            return Jsoup.parse(url.openStream(), "gb2312", urlStr);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return connectAndGetDocument(urlStr);
        }
    }


    public abstract List<Address> analyze(URI uri);
}
