package com.sogeti.carleasecustomer2api.mapper;

import com.sogeti.carleasecustomer2api.http.model.*;
import com.sogeti.carleasecustomer2api.model.Address;
import com.sogeti.carleasecustomer2api.model.Customer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {

    private final ModelMapper modelMapper;

        public Address AddressAddRequestToAddress(AddressAddRequest AddressAddRequest) {
        return modelMapper.map(AddressAddRequest, Address.class);
    }

    public Address AddressUpdateRequestToAddress(AddressUpdateRequest AddressUpdateRequest) {
        return modelMapper.map(AddressUpdateRequest, Address.class);
    }
}
