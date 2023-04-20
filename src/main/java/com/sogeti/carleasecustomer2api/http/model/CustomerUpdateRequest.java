package com.sogeti.carleasecustomer2api.http.model;

import lombok.Data;

@Data
public class CustomerUpdateRequest {
    private String name;
    private String email;
    private String phoneNumber;
}
