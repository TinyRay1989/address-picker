package com.tinyray.addresspicker.crawler.analyzer;

import com.tinyray.addresspicker.crawler.model.Address;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecondPageAnalyzer extends PageAnalyzer{

    private SubPageAnalyzer subPageAnalyzer = new SubPageAnalyzer();

    private final String beijing = "110000000000";
    private final String tianjin = "120000000000";
    private final String shanghai = "310000000000";
    private final String chongqing = "500000000000";

    @Override
    protected String getCss() {
        return "tr.citytr";
    }

    @Override
    public List<Address> analyze(URI uri) {

        List<Address> addressList = new ArrayList<>();
        Document subPage = connectAndGetDocument(uri.toString());
        Elements addresses = subPage.select(getCss());
        int size = addresses.size();
        for(Element addressElement: addresses){
            Elements datas = addressElement.select("a");

            Element idElement = datas.get(0);
            Element valueElement = datas.get(1);
            String href = idElement.attr("href");
            Pattern pattern = Pattern.compile("(\\d*/)(\\d*)(.html)");
            Matcher matcher = pattern.matcher(href);
            matcher.find();
            String id = idElement.ownText();
            String name = valueElement.ownText();

            System.out.println(">>正在爬取" + name + ">的数据…………");
            Address address = new Address();
            address.setChildURI(uri.resolve(href));
            address.setId(id);
            address.setName(name);

            URI childURI = address.getChildURI();
            List<Address> subAddressList = subPageAnalyzer.analyze(childURI);
            if (size <= 2) {//直辖市
                addressList.addAll(subAddressList);
            }else{
                addressList.add(address);
                address.setChild(subAddressList);
            }
        }
        return addressList;
    }
}
