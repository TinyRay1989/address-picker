package com.tinyray.addresspicker.crawler.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Address {
    private String id;
    private String name;
    private URI childURI;

    public List<Address> child = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getChildURI() {
        return childURI;
    }

    public void setChildURI(URI childURI) {
        this.childURI = childURI;
    }

    public List<Address> getChild() {
        return child;
    }

    public void setChild(List<Address> child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson(){
        final StringBuffer sb = new StringBuffer("{");
        sb.append("id:'").append(id.substring(0,6)).append('\'');
        sb.append(", name:'").append(name).append('\'');
        if(child.size() != 0)
        sb.append(", child:").append(child);
        sb.append('}');
        return sb.toString();
    }
}


