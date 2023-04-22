package com.sogeti.carleasecustomer2api.http.model;

import lombok.Data;

import java.util.List;

@Data
public class CustomerAddRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private List<AddressAddRequest> addresses;
}
