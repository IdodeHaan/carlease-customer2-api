package com.sogeti.carleasecustomer2api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private AddressType type;
    private String street;
    @Column(name = "house_number")
    private String houseNumber;
    @Column(name = "zip_code")
    private String zipCode;
    private String place;
}
