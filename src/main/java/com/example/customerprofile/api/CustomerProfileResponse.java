package com.example.customerprofile.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

record CustomerProfileResponse(@NotNull Long id, String firstName, String lastName, @NotBlank String email) {

    CustomerProfileResponse(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
