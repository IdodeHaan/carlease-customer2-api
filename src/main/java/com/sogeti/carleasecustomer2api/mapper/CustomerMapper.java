package com.sogeti.carleasecustomer2api.mapper;

import com.sogeti.carleasecustomer2api.model.Customer;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerAddRequest;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerResponse;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final ModelMapper modelMapper;

    public CustomerResponse customerToCustomerResponse(Customer customer) {
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public Customer customerAddRequestToCustomer(@Valid CustomerAddRequest customerAddRequest) {
        return modelMapper.map(customerAddRequest, Customer.class);
    }

    public Customer customerUpdateRequestToCustomer(CustomerUpdateRequest customerUpdateRequest) {
        return modelMapper.map(customerUpdateRequest, Customer.class);
    }
}
