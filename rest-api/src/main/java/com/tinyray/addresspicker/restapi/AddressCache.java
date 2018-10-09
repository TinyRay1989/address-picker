package com.tinyray.addresspicker.restapi;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class AddressCache {
    public static final int CODE_LENGTH = 6;
    private String path;
    private ConcurrentHashMap<String, Vector<CAddress>> addressMap = new ConcurrentHashMap<>();
    private Vector<CAddress> provinces = new Vector<>();


    public void setPath(String path) {
        this.path = path;
    }

    public void init() {
        File dataFile = new File(path);
        String dataStr = null;
        try {
            dataStr = FileUtils.readFileToString(dataFile, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray(dataStr);
        putProvinceData(jsonArray);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            if(jsonObject.has("child")){
                JSONArray chileArray = jsonObject.getJSONArray("child");
                putChildData(id, chileArray);
            }
        }
    }

    private void putChildData(String id, JSONArray childArray) {
        Vector<CAddress> vector = new Vector();
        for (int i = 0; i < childArray.length(); i++) {
            JSONObject jsonObject = childArray.getJSONObject(i);
            String childId = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            vector.add(new CAddress(childId, name));
            if(jsonObject.has("child")){
                JSONArray chileArray = jsonObject.getJSONArray("child");
                putChildData(childId, chileArray);
            }
        }
        addressMap.put(id, vector);
    }

    private void putProvinceData(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            provinces.add(new CAddress(id, name));
        }
    }

    public Vector<CAddress> getChild(String addressId){
        return addressMap.get(addressId);
    }

    public Vector<CAddress> getProvinces(){
        return provinces;
    }

    class CAddress{
        private String id;
        private String name;

        public CAddress(String id, String name) {
            this.id = id;
            this.name = name;
        }

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


        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("{");
            sb.append("id:'").append(id).append('\'');
            sb.append(", name:'").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
