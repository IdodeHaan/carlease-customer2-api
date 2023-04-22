package com.sogeti.carleasecustomer2api.controller;

import com.sogeti.carleasecustomer2api.http.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/customers")
public interface CustomerOperations {

    @GetMapping("/{customerId}")
    ResponseEntity<CustomerResponse> _getCustomerByIdV1(@PathVariable Long customerId);

    @GetMapping
    ResponseEntity<List<CustomerResponse>> _getCustomersV1(@RequestBody CustomerFilter filter);

    @PostMapping
    ResponseEntity<CustomerResponse> _createCustomerV1(@RequestBody CustomerAddRequest customerAddRequest);

    @PutMapping("/{customerId}")
    ResponseEntity<CustomerResponse> _updateCustomerV1(@PathVariable Long customerId, @RequestBody CustomerUpdateRequest customerUpdateRequest);

    @DeleteMapping("/{customerId}")
    ResponseEntity _deleteCustomerV1(@PathVariable Long customerId);
}
