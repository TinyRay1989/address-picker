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

public class SubPageAnalyzer extends PageAnalyzer {

    @Override
    protected String getCss() {
        return "tr.countytr";
    }

    @Override
    public List<Address> analyze(URI uri) {

        ArrayList<Address> addressList = new ArrayList<>();
        Document subPage = connectAndGetDocument(uri.toString());
        Elements addresses = subPage.select(getCss());
        for(Element addressElement: addresses){

            String id = null;
            String name = null;
            String href = null;
            Element idElement = null;
            Address address = new Address();
            try {
                Elements datas = addressElement.select("a");
                idElement = datas.get(0);
                Element valueElement = datas.get(1);
                href = idElement.attr("href");
                Pattern pattern = Pattern.compile("(\\d*/)(\\d*)(.html)");
                Matcher matcher = pattern.matcher(href);
                matcher.find();
                id = idElement.ownText();
                name = valueElement.ownText();
                address.setChildURI(uri.resolve(href));
            } catch (IndexOutOfBoundsException e) {

                Elements datas = addressElement.select("td");
                id = datas.get(0).ownText();
                name = datas.get(1).ownText();
            }

            address.setId(id);
            address.setName(name);
            addressList.add(address);

        }
        return addressList;
    }
}
