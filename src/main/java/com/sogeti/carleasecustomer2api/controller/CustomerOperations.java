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
    ResponseEntity<List<CustomerResponse>> _getAllCustomersV1();

    @PostMapping
    ResponseEntity<CustomerResponse> _createCustomerV1(@RequestBody CustomerAddRequest customerAddRequest);

    @PutMapping("/{customerId}")
    ResponseEntity<CustomerResponse> _updateCustomerV1(@PathVariable Long customerId, @RequestBody CustomerUpdateRequest customerUpdateRequest);

    @DeleteMapping("/{customerId}")
    ResponseEntity _deleteCustomerV1(@PathVariable Long customerId);

    @PostMapping("/{customerId}/addresses")
    ResponseEntity<CustomerResponse> _addAddressV1(@PathVariable Long customerId, @RequestBody AddressAddRequest addressAddRequest);

    @PutMapping("/{customerId}/addresses/{addressId}")
    ResponseEntity<CustomerResponse> _updateAddressV1(
            @PathVariable Long customerId, @PathVariable Long addressId, @RequestBody AddressUpdateRequest addressUpdateRequest);

    @DeleteMapping("/{customerId}/addresses/{addressId}")
    ResponseEntity _deleteAddressV1(@PathVariable Long customerId, @PathVariable Long addressId);
}
