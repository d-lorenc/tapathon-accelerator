package com.example.customerprofile.api;

import javax.validation.constraints.NotBlank;

record CustomerProfileCreateRequest(String firstName, String lastName, @NotBlank String email) {

    CustomerProfileCreateRequest(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
