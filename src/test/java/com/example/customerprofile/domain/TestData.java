package com.example.customerprofile.domain;

public class TestData {

    public static CustomerProfile testCustomerProfile() {
        return new CustomerProfile(123L, "Joe", "Doe", "joe.doe@test.org");
    }

    public static NewCustomerProfile testNewCustomerProfile() {
        return new NewCustomerProfile("Joe", "Doe", "joe.doe@test.org");
    }
}
