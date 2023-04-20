package com.sogeti.carleasecustomer2api.repository;

import com.sogeti.carleasecustomer2api.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
