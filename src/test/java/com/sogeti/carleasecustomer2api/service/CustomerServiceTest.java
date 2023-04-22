package com.sogeti.carleasecustomer2api.service;

import com.sogeti.carleasecustomer2api.exception.ResourceNotFoundException;
import com.sogeti.carleasecustomer2api.model.Address;
import com.sogeti.carleasecustomer2api.model.AddressType;
import com.sogeti.carleasecustomer2api.model.Customer;
import com.sogeti.carleasecustomer2api.repository.AddressRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    AddressRepository addressRepository;
    private Customer addedCustomer;
    private String name = "John Doe";
    private String email = "john@example.com";
    private String phoneNumber = "+31612345678";
    private AddressType type = AddressType.WORK;
    private String street = "Some Street";
    private String houseNumber = "55b";
    private String zipCode = "4455 ZZ";
    private String place = "Some Place";
    private Address address;
    @BeforeEach
    void setup() {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);

        address = new Address();
        address.setType(type);
        address.setStreet(street);
        address.setHouseNumber(houseNumber);
        address.setZipCode(zipCode);
        address.setPlace(place);
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
    void testAddCustomer_whenCorrectCustomerIsGiven_returnedCustomerHasId() {
        //then
        assertTrue(addedCustomer.getId() > 0);
        assertEquals(name, addedCustomer.getName());
        assertEquals(email, addedCustomer.getEmail());
        assertEquals(phoneNumber, addedCustomer.getPhoneNumber());
        Address createdAddress = addedCustomer.getAddresses().get(0);
        assertTrue(createdAddress.getId() > 0);
        assertEquals(type, createdAddress.getType());
        assertEquals(street, createdAddress.getStreet());
        assertEquals(houseNumber, createdAddress.getHouseNumber());
        assertEquals(zipCode, createdAddress.getZipCode());
        assertEquals(place, createdAddress.getPlace());
    }

    @Test
    @DisplayName("retrieve customer by id")
    @Transactional
    void testRetrieve_whenCustomerWithGivenIdExists_CustomerMustBeReturned() throws ResourceNotFoundException {
        //when
        Customer foundCustomer = customerService.retrieve(addedCustomer.getId());
        assertEquals(name, foundCustomer.getName());
        assertEquals(email, foundCustomer.getEmail());
        assertEquals(phoneNumber, foundCustomer.getPhoneNumber());
        Address foundAddress = foundCustomer.getAddresses().get(0);
        assertTrue(foundAddress.getId() > 0);
        assertEquals(type, foundAddress.getType());
        assertEquals(street, foundAddress.getStreet());
        assertEquals(houseNumber, foundAddress.getHouseNumber());
        assertEquals(zipCode, foundAddress.getZipCode());
        assertEquals(place, foundAddress.getPlace());
    }

    @Test
    @DisplayName("retrieve non-existing customer")
    void testRetrieve_whenCustomerWithGivenIdDoesNotExist_ResourceNotFoundExceptionIsThrown() {
        //given
        Long idOfNonExistingCustomer = addedCustomer.getId() + 1;
        //when & then
        assertThrows(ResourceNotFoundException.class, () -> customerService.retrieve(idOfNonExistingCustomer)
                , "Retrieve with non-existing id should throw ResourceNotFoundException");
    }

    @Test
    @DisplayName("retrieve all customers")
    void testRetrieveAll_whenACustomerIsStored_OneMoreCustomerIsReturned() {
        //given
        int numberOfStoredCustomers = customerService.retrieveAll().size();
        Customer customer = new Customer();
        customer.setName("Joan");
        customer.setEmail("joan@example.com");
        customer.setPhoneNumber("+33688776655");
        customerService.add(customer);
        //when
        List<Customer> customers = customerService.retrieveAll();
        //then
        assertEquals(numberOfStoredCustomers + 1, customers.size());
    }

    @Test
    @DisplayName("update customer details")
    void testUpdate_whenCustomerDetailsAreUpdated_UpdateDetailsAreStored() throws ResourceNotFoundException {
        //given
        String newName = "New name";
        String newEmail = "newname@example.com";
        String newPhoneNumber = "new phoneNumber";
        addedCustomer.setName(newName);
        addedCustomer.setEmail(newEmail);
        addedCustomer.setPhoneNumber(newPhoneNumber);
        Address addedAddress = addedCustomer.getAddresses().get(0);
        AddressType type1 = AddressType.CORRESPONDENCE;
        String street1 = "Another Street";
        String houseNumber1 = "999x";
        String zipCode1 = "9999ZZ";
        String place1 = "Another Place";
        addedAddress.setType(type1);
        addedAddress.setStreet(street1);
        addedAddress.setHouseNumber(houseNumber1);
        addedAddress.setZipCode(zipCode1);
        addedAddress.setPlace(place1);
        //when
        Customer updatedCustomer = customerService.update(addedCustomer.getId(), addedCustomer);
        //then
        assertEquals(newName, updatedCustomer.getName());
        assertEquals(newEmail, updatedCustomer.getEmail());
        assertEquals(newPhoneNumber, updatedCustomer.getPhoneNumber());
        Address updatedAddress = updatedCustomer.getAddresses().get(0);
        assertEquals(addedAddress.getId(), updatedAddress.getId());
        assertEquals(type1, updatedAddress.getType());
        assertEquals(street1, updatedAddress.getStreet());
        assertEquals(houseNumber1, updatedAddress.getHouseNumber());
        assertEquals(zipCode1, updatedAddress.getZipCode());
        assertEquals(place1, updatedAddress.getPlace());
    }

    @Test
    @DisplayName("update non-existing customer")
    void testUpdate_whenCustomerWithGivenIdDoesNotExist_ResourceNotFoundExceptionIsThrown() {
        //given
        Long idOfNonExistingCustomer = addedCustomer.getId() + 1;
        //when & then
        assertThrows(ResourceNotFoundException.class, () -> customerService.update(idOfNonExistingCustomer, addedCustomer)
                , "Update with non-existing id should throw ResourceNotFoundException");
    }

    @Test
    @DisplayName("delete customer with its addresses")
    void testDelete_whenGivenCustomerExists_CustomerIsDeleted() throws ResourceNotFoundException {
        //when
        long customerId = addedCustomer.getId();
        long addressId = addedCustomer.getAddresses().get(0).getId();
        customerService.delete(customerId);
        //then
        assertThrows(ResourceNotFoundException.class, () -> customerService.retrieve(customerId)
                , "Retrieve a deleted customer should throw ResourceNotFoundException");
        assertThrows(NoSuchElementException.class, () -> addressRepository.findById(addressId).get()
        ,"Retrieve an address of a deleted customer should throw an Exception");
    }

    @Test
    @DisplayName("delete non-existing customer")
    void testDelete_whenCustomerWithGivenIdDoesNotExist_ResourceNotFoundExceptionIsThrown() {
        //given
        Long idOfNonExistingCustomer = addedCustomer.getId() + 1;
        //when & then
        assertThrows(ResourceNotFoundException.class, () -> customerService.delete(idOfNonExistingCustomer)
                , "Delete with non-existing id should throw ResourceNotFoundException");
    }
}