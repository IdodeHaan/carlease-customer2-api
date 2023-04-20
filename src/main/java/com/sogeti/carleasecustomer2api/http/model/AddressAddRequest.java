package com.sogeti.carleasecustomer2api.http.model;

import com.sogeti.carleasecustomer2api.model.AddressType;
import lombok.Data;

@Data
public class AddressAddRequest {

    private AddressType type;
    private String street;
    private String houseNumber;
    private String zipCode;
    private String place;
}
