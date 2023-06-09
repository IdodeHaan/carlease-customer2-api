package com.sogeti.carleasecustomer2api.service;

import com.sogeti.carleasecustomer2api.exceptionhandling.ResourceNotFoundException;
import com.sogeti.carleasecustomer2api.model.Customer;
import com.sogeti.carleasecustomer2api.repository.CustomerRepository;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerFilter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<Customer> retrieveCustomers(CustomerFilter filter) {
        Pageable pageRequest = PageRequest.of(filter.getPage(), filter.getSize());
        if (filter.getEmail() != null) {
            return customerRepository.findByEmail(filter.getEmail(), pageRequest);
        } else {
            return customerRepository.findAll(pageRequest);
        }
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
