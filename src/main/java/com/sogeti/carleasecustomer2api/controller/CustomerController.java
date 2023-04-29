package com.sogeti.carleasecustomer2api.controller;

import com.sogeti.carleasecustomer2api.exceptionhandling.InputValidationException;
import com.sogeti.carleasecustomer2api.exceptionhandling.OtherException;
import com.sogeti.carleasecustomer2api.exceptionhandling.ResourceNotFoundException;
import com.sogeti.carleasecustomer2api.mapper.CustomerMapper;
import com.sogeti.carleasecustomer2api.model.Customer;
import com.sogeti.carleasecustomer2api.service.CustomerService;
import com.sogeti.carleasecustomercontractapi.openapi.api.V1Api;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerAddRequest;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerFilter;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerResponse;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerUpdateRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CustomerController implements V1Api {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;


    @Override
    public ResponseEntity<CustomerResponse> createCustomerV1(CustomerAddRequest customerAddRequest) {
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
    public ResponseEntity<List<CustomerResponse>> getCustomersV1(CustomerFilter customerFilter) {
        List<Customer> customers = customerService.retrieveCustomers(customerFilter);
        List<CustomerResponse> customerResponses = customers.stream()
                .map(customerMapper::customerToCustomerResponse)
                .collect(Collectors.toList());
        if (customerResponses.isEmpty()) {
            throw new ResourceNotFoundException("No customers found");
        }
        return ResponseEntity.ok(customerResponses);
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
