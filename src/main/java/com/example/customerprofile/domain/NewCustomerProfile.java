package com.example.customerprofile.domain;

import javax.validation.constraints.NotBlank;

public record NewCustomerProfile(String firstName, String lastName, @NotBlank String email) {

    public NewCustomerProfile(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
