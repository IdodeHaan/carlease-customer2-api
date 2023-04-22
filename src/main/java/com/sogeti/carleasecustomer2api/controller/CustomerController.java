package com.sogeti.carleasecustomer2api.controller;

import com.sogeti.carleasecustomer2api.exception.ResourceNotFoundException;
import com.sogeti.carleasecustomer2api.http.model.CustomerAddRequest;
import com.sogeti.carleasecustomer2api.http.model.CustomerFilter;
import com.sogeti.carleasecustomer2api.http.model.CustomerResponse;
import com.sogeti.carleasecustomer2api.http.model.CustomerUpdateRequest;
import com.sogeti.carleasecustomer2api.mapper.CustomerMapper;
import com.sogeti.carleasecustomer2api.model.Customer;
import com.sogeti.carleasecustomer2api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerOperations{

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public ResponseEntity<CustomerResponse> _getCustomerByIdV1(@PathVariable Long customerId) {
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
    public ResponseEntity<List<CustomerResponse>> _getCustomersV1(CustomerFilter filter) {
        List<Customer> customers = customerService.retrieveCustomers(filter);
        List<CustomerResponse> customerResponses = customers.stream()
                .map(customerMapper::customerToCustomerResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerResponses);
    }

    public ResponseEntity<CustomerResponse> _createCustomerV1(@RequestBody CustomerAddRequest customerAddRequest) {
        try {
            Customer customer = customerMapper.customerAddRequestToCustomer(customerAddRequest);
            Customer createdCustomer = customerService.add(customer);
            CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(createdCustomer);
            return ResponseEntity.ok(customerResponse);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<CustomerResponse> _updateCustomerV1(@PathVariable Long customerId, @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        try {
            Customer customer = customerMapper.customerUpdateRequestToCustomer(customerUpdateRequest);
            Customer updatedCustomer = customerService.update(customerId, customer);
            CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(updatedCustomer);
            return ResponseEntity.ok(customerResponse);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity _deleteCustomerV1(@PathVariable Long customerId) {
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
}
