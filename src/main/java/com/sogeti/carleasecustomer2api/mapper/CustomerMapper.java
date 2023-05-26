package com.sogeti.carleasecustomer2api.mapper;

import com.sogeti.carleasecustomer2api.model.Customer;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerAddRequest;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerResponse;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerResponsePage;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
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

    public CustomerResponsePage pageCustomerToCustomerResponsePage(Page<Customer> page) {
        CustomerResponsePage responsePage = new CustomerResponsePage();
        Page<CustomerResponse> customerResponses = page.map(this::customerToCustomerResponse);
        responsePage.setTotalRecords(page.getTotalElements());
        responsePage.setPage(page.getNumber());
        responsePage.setSize(page.getSize());
        responsePage.setCustomers(customerResponses.getContent());
        return responsePage;
    }
}
