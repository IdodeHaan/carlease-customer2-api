package com.sogeti.carleasecustomer2api.service;

import com.sogeti.carleasecustomer2api.exception.ResourceNotFoundException;
import com.sogeti.carleasecustomer2api.model.Address;
import com.sogeti.carleasecustomer2api.model.AddressType;
import com.sogeti.carleasecustomer2api.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    CustomerService customerService;
    private String name = "John Doe";
    private String email = "john@example.com";
    private String phoneNumber = "+31612345678";
    private Customer addedCustomer;
    private AddressType type = AddressType.WORK;
    private String street = "Some Street";
    private String houseNumber = "55b";
    private String zipCode = "4455 ZZ";
    private String place = "Some Place";
    private Address address;
    @BeforeEach
    void setup() throws ResourceNotFoundException {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        addedCustomer = customerService.add(customer);

        address = new Address();
        address.setType(type);
        address.setStreet(street);
        address.setHouseNumber(houseNumber);
        address.setZipCode(zipCode);
        address.setPlace(place);
    }

    @Test
    @DisplayName("add customer")
    void testAddCustomer_whenCorrectCustomerIsGiven_returnedCustomerHasId() {
        //then
        assertTrue(addedCustomer.getId() > 0);
        assertEquals(name, addedCustomer.getName());
        assertEquals(email, addedCustomer.getEmail());
        assertEquals(phoneNumber, addedCustomer.getPhoneNumber());
    }

    @Test
    @DisplayName("retrieve customer by id")
    void testRetrieve_whenCustomerWithGivenIdExists_CustomerMustBeReturned() throws ResourceNotFoundException {
        //when
        Customer foundCustomer = customerService.retrieve(addedCustomer.getId());
        assertEquals(name, foundCustomer.getName());
        assertEquals(email, foundCustomer.getEmail());
        assertEquals(phoneNumber, foundCustomer.getPhoneNumber());
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
        //when
        Customer updatedCustomer = customerService.update(addedCustomer.getId(), addedCustomer);
        //then
        assertEquals(newName, updatedCustomer.getName());
        assertEquals(newEmail, updatedCustomer.getEmail());
        assertEquals(newPhoneNumber, updatedCustomer.getPhoneNumber());
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
    @DisplayName("delete customer")
    void testDelete_whenGivenCustomerExists_CustomerIsDeleted() throws ResourceNotFoundException {
        //when
        long customerId = addedCustomer.getId();
        customerService.delete(customerId);
        //then
        assertThrows(ResourceNotFoundException.class, () -> customerService.retrieve(customerId)
                , "Retrieve a deleted customer should throw ResourceNotFoundException");
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

    @Test
    @DisplayName("add address")
    @Transactional
    void testAddAddress_whenCorrectDetailsAreGiven_returnedAddressHadId() throws ResourceNotFoundException {
        //when
        addedCustomer = customerService.addAddress(addedCustomer.getId(), address);
        Address createdAddress = addedCustomer.getAddresses().get(0);
        //then
        assertTrue(createdAddress.getId() > 0);
        assertEquals(type, createdAddress.getType());
        assertEquals(street, createdAddress.getStreet());
        assertEquals(houseNumber, createdAddress.getHouseNumber());
        assertEquals(zipCode, createdAddress.getZipCode());
        assertEquals(place, createdAddress.getPlace());
    }

    @Test
    @DisplayName("add address to non-existing customer")
    void testAddAddress_whenCustomerDoesNotExist_throwsResourceNotFoundException() {
        //given
        Long idOfNonExistingCustomer = addedCustomer.getId() + 1;
        //when and then
        assertThrows(ResourceNotFoundException.class, () -> customerService.addAddress(idOfNonExistingCustomer, address)
        , "adding address to non-existing customer should throw ResourceNotFoundException");
    }

    @Test
    @DisplayName("update address")
    @Transactional
    void testUpdateAddress_whenAddressDetailsAreUpdated_detailsAreCorrectlyStored() throws ResourceNotFoundException {
        //given
        addedCustomer = customerService.addAddress(addedCustomer.getId(), address);
        Address createdAddress = addedCustomer.getAddresses().get(0);
        AddressType type1 = AddressType.CORRESPONDENCE;
        String street1 = "Another Street";
        String houseNumber1 = "999x";
        String zipCode1 = "9999ZZ";
        String place1 = "Another Place";
        createdAddress.setType(type1);
        createdAddress.setStreet(street1);
        createdAddress.setHouseNumber(houseNumber1);
        createdAddress.setZipCode(zipCode1);
        createdAddress.setPlace(place1);
        //when
        Customer customer = customerService.updateAddress(addedCustomer.getId(), createdAddress.getId(), createdAddress);
        Address updatedAddress = customer.getAddresses().get(0);
        //then
        assertEquals(createdAddress.getId(), updatedAddress.getId());
        assertEquals(type1, updatedAddress.getType());
        assertEquals(street1, updatedAddress.getStreet());
        assertEquals(houseNumber1, updatedAddress.getHouseNumber());
        assertEquals(zipCode1, updatedAddress.getZipCode());
        assertEquals(place1, updatedAddress.getPlace());
    }

    @Test
    @DisplayName("update address of non-existing customer")
    @Transactional
    void testUpdateAddress__whenCustomerDoesNotExist_throwsResourceNotFoundException() throws ResourceNotFoundException {
        //given
        addedCustomer = customerService.addAddress(addedCustomer.getId(), address);
        Address createdAddress = addedCustomer.getAddresses().get(0);
        Long idOfNonExistingCustomer = addedCustomer.getId() + 1;
        //when & then
        assertThrows(ResourceNotFoundException.class,
                () -> customerService.updateAddress(idOfNonExistingCustomer, createdAddress.getId(), address)
        , "updating address of non-existing customer should throw ResourceNotFoundException");
    }

    @Test
    @DisplayName("update non-existing address")
    @Transactional
    void testUpdateAddress__whenAddressDoesNotExist_throwsResourceNotFoundException() throws ResourceNotFoundException {
        //given
        addedCustomer = customerService.addAddress(addedCustomer.getId(), address);
        Address createdAddress = addedCustomer.getAddresses().get(0);
        Long idOfNonExistingAddress = createdAddress.getId() + 1;
        //when & then
        assertThrows(ResourceNotFoundException.class,
                () -> customerService.updateAddress(addedCustomer.getId(), idOfNonExistingAddress, address)
                , "updating an address a non-existing address should throw ResourceNotFoundException");
    }

    @Test
    @DisplayName("add address")
    @Transactional
    void testDeleteAddress_whenAddressIsDeleted_deletingItAgainThrowsException() throws ResourceNotFoundException {
        //given
        addedCustomer = customerService.addAddress(addedCustomer.getId(), address);
        Address createdAddress = addedCustomer.getAddresses().get(0);
        Long idOfDeletedAddress = createdAddress.getId();
        //when
        customerService.deleteAddress(addedCustomer.getId(), idOfDeletedAddress);
        //then
        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteAddress(addedCustomer.getId(), idOfDeletedAddress)
        , "deleting an address that already is deleted should throw ResourceNotFoundException");
    }

    @Test
    @DisplayName("delete address of non-existing customer")
    @Transactional
    void testDeleteAddress__whenCustomerDoesNotExist_throwsResourceNotFoundException() throws ResourceNotFoundException {
        //given
        addedCustomer = customerService.addAddress(addedCustomer.getId(), address);
        Address createdAddress = addedCustomer.getAddresses().get(0);
        Long idOfNonExistingCustomer = addedCustomer.getId() + 1;
        //when & then
        assertThrows(ResourceNotFoundException.class,
                () -> customerService.deleteAddress(idOfNonExistingCustomer, createdAddress.getId())
                , "deleting address of non-existing customer should throw ResourceNotFoundException");
    }
}