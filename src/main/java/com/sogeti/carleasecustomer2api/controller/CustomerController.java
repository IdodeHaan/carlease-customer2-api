package com.sogeti.carleasecustomer2api.controller;

import com.sogeti.carleasecustomer2api.exception.ResourceNotFoundException;
import com.sogeti.carleasecustomer2api.mapper.CustomerMapper;
import com.sogeti.carleasecustomer2api.model.Customer;
import com.sogeti.carleasecustomer2api.service.CustomerService;
import com.sogeti.carleasecustomercontractapi.openapi.api.V1Api;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerAddRequest;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerFilter;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerResponse;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CustomerController implements V1Api {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;


    @Override
    public ResponseEntity<CustomerResponse> createCustomerV1(@Valid CustomerAddRequest customerAddRequest) {
        try {
            Customer customer = customerMapper.customerAddRequestToCustomer(customerAddRequest);
            Customer createdCustomer = customerService.add(customer);
            CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(createdCustomer);
            return ResponseEntity.ok(customerResponse);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteCustomerByIdV1(Long customerId) {
        try {
            customerService.delete(customerId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer with id " + customerId + " not found", exception);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<CustomerResponse> getCustomerByIdV1(Long customerId) {
        try {
            Customer customer = customerService.retrieve(customerId);
            CustomerResponse customerResponse =
                    customerMapper.customerToCustomerResponse(customer);
            return ResponseEntity.ok(customerResponse);
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer with id " + customerId + " not found", exception);
        }
    }

    @Override
    public ResponseEntity<List<CustomerResponse>> getCustomersV1(@Valid CustomerFilter customerFilter) {
        List<Customer> customers = customerService.retrieveCustomers(customerFilter);
        List<CustomerResponse> customerResponses = customers.stream()
                .map(customerMapper::customerToCustomerResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerResponses);
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomerV1(Long customerId, @Valid CustomerUpdateRequest customerUpdateRequest) {
        try {
            Customer customer = customerMapper.customerUpdateRequestToCustomer(customerUpdateRequest);
            Customer updatedCustomer = customerService.update(customerId, customer);
            CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(updatedCustomer);
            return ResponseEntity.ok(customerResponse);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
