package com.tinyray.addresspicker.restapi.controller;

import com.tinyray.addresspicker.crawler.model.Address;
import com.tinyray.addresspicker.restapi.AddressCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Vector;

@RestController
public class AddressController {

    @Autowired
    private AddressCache addressCache;

    @RequestMapping(
            path = "address/{id}/child",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
    )
    public ResponseEntity<List<Address>> getChildAddresses(@PathVariable String id){
        Vector child = addressCache.getChild(id);
        if(StringUtils.isEmpty(id) || id.length() != AddressCache.CODE_LENGTH){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(child);
    }


    @RequestMapping(
            path = "provinces",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
    )
    public ResponseEntity<List<Address>> getProvinces(){
        Vector provinces = addressCache.getProvinces();
        return ResponseEntity.ok(provinces);
    }

}
