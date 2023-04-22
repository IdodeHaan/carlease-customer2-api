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
}
