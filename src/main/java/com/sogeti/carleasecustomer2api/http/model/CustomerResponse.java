package com.sogeti.carleasecustomer2api.http.model;

import com.sogeti.carleasecustomer2api.model.Address;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CustomerResponse {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    List<Address> addresses;
}
