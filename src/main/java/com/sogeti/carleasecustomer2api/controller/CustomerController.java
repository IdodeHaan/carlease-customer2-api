package com.sogeti.carleasecustomer2api.controller;

import com.sogeti.carleasecustomer2api.exceptionhandling.InputValidationException;
import com.sogeti.carleasecustomer2api.exceptionhandling.OtherException;
import com.sogeti.carleasecustomer2api.exceptionhandling.ResourceNotFoundException;
import com.sogeti.carleasecustomer2api.mapper.CustomerMapper;
import com.sogeti.carleasecustomer2api.model.Customer;
import com.sogeti.carleasecustomer2api.service.CustomerService;
import com.sogeti.carleasecustomercontractapi.openapi.api.V1Api;
import com.sogeti.carleasecustomercontractapi.openapi.model.*;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
        } catch (ConstraintViolationException exception) {
            throw new InputValidationException(exception);
        } catch (Exception exception) {
            throw new OtherException(exception);
        }
    }

        @Override
    public ResponseEntity<Void> deleteCustomerByIdV1(Long customerId) {
        try {
            customerService.delete(customerId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException exception) {
            throw new ResourceNotFoundException("Customer id not found - " + customerId);
        } catch (Exception exception) {
            throw new OtherException(exception);
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
            throw new ResourceNotFoundException("Customer id not found - " + customerId);
        } catch (Exception exception) {
            throw new OtherException(exception);
        }
    }

    @Override
    public ResponseEntity<CustomerResponsePage> getCustomersV1(@Valid CustomerFilter customerFilter) {
        Page<Customer> customers = customerService.retrieveCustomers(customerFilter);
        if (customers.getContent().isEmpty()) {
            throw new ResourceNotFoundException("No customers found");
        }
        CustomerResponsePage responsePage = customerMapper.pageCustomerToCustomerResponsePage(customers);
        return ResponseEntity.ok(responsePage);
    }

    @Override
    public ResponseEntity<CustomerResponse> updateCustomerV1(Long customerId, @Valid CustomerUpdateRequest customerUpdateRequest) {
        try {
            Customer customer = customerMapper.customerUpdateRequestToCustomer(customerUpdateRequest);
            Customer updatedCustomer = customerService.update(customerId, customer);
            CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(updatedCustomer);
            return ResponseEntity.ok(customerResponse);
        } catch (ConstraintViolationException exception) {
            throw new InputValidationException(exception);
        } catch (ResourceNotFoundException exception) {
            throw new ResourceNotFoundException("Customer id not found - " + customerId);
        } catch (Exception exception) {
            throw new OtherException(exception);
        }
    }
}
