package com.sogeti.carleasecustomer2api.http.model;

import com.sogeti.carleasecustomer2api.model.Address;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CustomerAddRequest {
    private String name;
    private String email;
    private String phoneNumber;
}
