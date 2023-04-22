package com.sogeti.carleasecustomer2api.http.model;

import lombok.Data;

import java.util.List;

@Data
public class CustomerUpdateRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private List<AddressUpdateRequest> addresses;
}
