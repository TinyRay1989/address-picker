package com.tinyray.addresspicker.crawler.analyzer;

import com.tinyray.addresspicker.crawler.model.Address;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FirstPageAnalyzer extends PageAnalyzer {
    private String css = "provincetr";

    private SecondPageAnalyzer subPageAnalyzer = new SecondPageAnalyzer();
    @Override
    public List<Address> analyze(URI uri){

        List<Address> addressList = new ArrayList<>();

        Document firstPage = connectAndGetDocument(uri.toString());
        Elements provinceTrs = firstPage.select("tr." + getCss());
        for(Element e : provinceTrs){
            Elements elements = e.select("a");
            for(Element province : elements){
                String href = province.selectFirst("a").attr("href");
                String id = href.substring(0,2).concat("0000000000");
                String name = province.text();
                Address address = new Address();
                address.setId(id);
                address.setName(name);
                address.setChildURI(uri.resolve(href));
                addressList.add(address);


                URI childURI = address.getChildURI();

                System.out.println(">>>>正在爬取" + name + ">的数据…………");
                List<Address> subAddressList = subPageAnalyzer.analyze(childURI);
                address.setChild(subAddressList);

            }
        }
        return addressList;
    }
}
