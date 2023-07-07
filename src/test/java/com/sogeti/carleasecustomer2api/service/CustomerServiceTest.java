package com.sogeti.carleasecustomer2api.service;

import com.sogeti.carleasecustomer2api.exceptionhandling.ResourceNotFoundException;
import com.sogeti.carleasecustomer2api.model.Address;
import com.sogeti.carleasecustomer2api.model.AddressType;
import com.sogeti.carleasecustomer2api.model.Customer;
import com.sogeti.carleasecustomer2api.repository.AddressRepository;
import com.sogeti.carleasecustomercontractapi.openapi.model.CustomerFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    AddressRepository addressRepository;
    private Customer customer;
    private Customer addedCustomer;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setPhoneNumber("+31612345678");

        Address address = new Address();
        address.setType(AddressType.WORK);
        address.setStreet("Some Street");
        address.setHouseNumber("55b");
        address.setZipCode("4455 ZZ");
        address.setPlace("Some Place");
        customer.addAddress(address);

        addedCustomer = customerService.add(customer);
    }

    @AfterEach
    void cleanup() {
        try {
            customerService.delete(addedCustomer.getId());
        } catch (ResourceNotFoundException ignored) {

        }
    }

    @Test
    @DisplayName("add customer")
    void testAddCustomer_whenCorrectCustomerIsGiven_returnedCustomerHasSameDetails() {
        //when
        customer.setId(addedCustomer.getId());
        customer.getAddresses().get(0).setId(addedCustomer.getAddresses().get(0).getId());
        //then
        assertThat(customer)
                .as("all details of the inserted customer must be equal to the original details, except the id's")
                .usingRecursiveComparison()
                .isEqualTo(addedCustomer);
    }

    @Test
    @DisplayName("retrieve customer by id")
    @Transactional
    void testRetrieve_whenCustomerWithGivenIdExists_CustomerMustBeReturned() throws ResourceNotFoundException {
        //when
        Customer foundCustomer = customerService.retrieve(addedCustomer.getId());
        //then
        assertThat(addedCustomer)
                .usingRecursiveComparison()
                .isEqualTo(foundCustomer);
    }

    @Test
    @DisplayName("retrieve non-existing customer")
    void testRetrieve_whenCustomerWithGivenIdDoesNotExist_ResourceNotFoundExceptionIsThrown() {
        //given
        Long idOfNonExistingCustomer = addedCustomer.getId() + 1;
        //when & then
        assertThatThrownBy(() -> customerService.retrieve(idOfNonExistingCustomer))
                .as("Retrieve with non-existing id should throw ResourceNotFoundException")
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("retrieve all customers")
    @Transactional
    //Todo fix unit tests
    void testRetrieveAll_whenACustomerIsStored_OneMoreCustomerIsReturned() {
        //given
        CustomerFilter filter = new CustomerFilter();
        filter.setEmail(null);
        int numberOfStoredCustomers = customerService.retrieveCustomers(filter).size();
        Customer customer = new Customer();
        customer.setName("Joan");
        customer.setEmail("joan@example.com");
        customer.setPhoneNumber("+33688776655");
        customerService.add(customer);
        //when
        List<Customer> customers = customerService.retrieveCustomers(filter);
        //then
        assertThat(customers.size())
                .isEqualTo(numberOfStoredCustomers + 1);
    }

    @Test
    @DisplayName("filter customers on email")
    @Transactional
    void testRetrieveAll_whenFilteredOnEmail_onlyCustomersWithGivenEmailAreReturned() {
        //given
        CustomerFilter filter = new CustomerFilter();
        String email1 = "joan@example.com";
        filter.setEmail(email1);
        Customer customer = new Customer();
        customer.setName("Joan");
        customer.setEmail(email1);
        customer.setPhoneNumber("+33688776655");
        customerService.add(customer);
        //when
        List<Customer> customers = customerService.retrieveCustomers(filter);
        //then
        for (Customer c : customers) {
            assertThat(c.getEmail()).isEqualTo(email1);
        }
    }

    @Test
    @DisplayName("update customer details")
    void testUpdate_whenCustomerDetailsAreUpdated_UpdateDetailsAreStored() throws ResourceNotFoundException {
        //given
        addedCustomer.setName("New name");
        addedCustomer.setEmail("newname@example.com");
        addedCustomer.setPhoneNumber("new phoneNumber");
        Address addedAddress = addedCustomer.getAddresses().get(0);
        addedAddress.setType(AddressType.CORRESPONDENCE);
        addedAddress.setStreet("Another Street");
        addedAddress.setHouseNumber("999x");
        addedAddress.setZipCode("9999ZZ");
        addedAddress.setPlace("Another Place");
        //when
        Customer updatedCustomer = customerService.update(addedCustomer.getId(), addedCustomer);
        //then
        assertThat(updatedCustomer)
                .usingRecursiveComparison()
                .isEqualTo(addedCustomer);
    }

    @Test
    @DisplayName("update non-existing customer")
    void testUpdate_whenCustomerWithGivenIdDoesNotExist_ResourceNotFoundExceptionIsThrown() {
        //given
        Long idOfNonExistingCustomer = addedCustomer.getId() + 1;
        //when & then
        assertThatThrownBy(() -> customerService.update(idOfNonExistingCustomer, addedCustomer))
                .as("Update with non-existing id should throw ResourceNotFoundException")
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("delete customer with its addresses")
    void testDelete_whenGivenCustomerExists_CustomerAndAddressesAreDeleted() throws ResourceNotFoundException {
        //when
        long customerId = addedCustomer.getId();
        long addressId = addedCustomer.getAddresses().get(0).getId();
        customerService.delete(customerId);
        //then
        assertThatThrownBy(() -> customerService.retrieve(customerId))
                .as("Retrieve a deleted customer should throw ResourceNotFoundException")
                .isInstanceOf(ResourceNotFoundException.class);
        assertThatThrownBy(() -> addressRepository.findById(addressId).get())
                .as("Retrieve an address of a deleted customer should throw an Exception")
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("delete non-existing customer")
    void testDelete_whenCustomerWithGivenIdDoesNotExist_ResourceNotFoundExceptionIsThrown() {
        //given
        Long idOfNonExistingCustomer = addedCustomer.getId() + 1;
        //when & then
        assertThatThrownBy(() -> customerService.delete(idOfNonExistingCustomer))
                .as("Delete with non-existing id should throw ResourceNotFoundException")
                .isInstanceOf(ResourceNotFoundException.class);
    }
}