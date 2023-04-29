package com.sogeti.carleasecustomer2api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private AddressType type;

    @NotBlank(message = "street is mandatory")
    private String street;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "zip_code")
    @NotBlank(message = "zipcode is mandatory")
    private String zipCode;

    @NotBlank(message = "place is mandatory")
    private String place;
}
