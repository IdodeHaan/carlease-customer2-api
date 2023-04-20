package com.sogeti.carleasecustomer2api.service;

import com.sogeti.carleasecustomer2api.exception.ResourceNotFoundException;
import com.sogeti.carleasecustomer2api.model.Address;
import com.sogeti.carleasecustomer2api.model.Customer;
import com.sogeti.carleasecustomer2api.repository.AddressRepository;
import com.sogeti.carleasecustomer2api.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public Customer retrieve(Long id) throws ResourceNotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<Customer> retrieveAll() {
        return customerRepository.findAll();
    }

    public Customer add(Customer customer) {
        customer.setId(0L);
        return customerRepository.save(customer);
    }

    public Customer update(Long id, Customer customer) throws ResourceNotFoundException {
        retrieve(id);
        customer.setId(id);
        return customerRepository.save(customer);
    }

    public void delete(Long id) throws ResourceNotFoundException {
        retrieve(id);
        customerRepository.deleteById(id);
    }

    public Customer addAddress(Long customerId, Address address) throws ResourceNotFoundException {
        Customer _customer = retrieve(customerId);
        _customer.addAddress(address);
        return customerRepository.save(_customer);
    }

    public Customer updateAddress(Long customerId, Long addressId, Address address) throws ResourceNotFoundException {
        Customer customer = retrieve(customerId);
        addressRepository.findById(addressId).orElseThrow(ResourceNotFoundException::new);
        address.setId(addressId);
        addressRepository.save(address);
        return retrieve(customerId);
    }

    public Customer deleteAddress(Long customerId, Long addressId) throws ResourceNotFoundException {
        Customer customer = retrieve(customerId);
        addressRepository.findById(addressId).orElseThrow(ResourceNotFoundException::new);
        addressRepository.deleteById(addressId);
        return retrieve(customerId);
    }
}
