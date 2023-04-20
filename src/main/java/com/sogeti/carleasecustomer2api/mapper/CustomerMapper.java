package com.sogeti.carleasecustomer2api.mapper;

import com.sogeti.carleasecustomer2api.http.model.CustomerAddRequest;
import com.sogeti.carleasecustomer2api.http.model.CustomerResponse;
import com.sogeti.carleasecustomer2api.http.model.CustomerUpdateRequest;
import com.sogeti.carleasecustomer2api.model.Customer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final ModelMapper modelMapper;

    public CustomerResponse customerToCustomerResponse(Customer customer) {
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public Customer customerAddRequestToCustomer(CustomerAddRequest customerAddRequest) {
        return modelMapper.map(customerAddRequest, Customer.class);
    }

    public Customer customerUpdateRequestToCustomer(CustomerUpdateRequest customerUpdateRequest) {
        return modelMapper.map(customerUpdateRequest, Customer.class);
    }
}
