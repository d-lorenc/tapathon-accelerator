package com.example.customerprofile.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CustomerProfile(@NotNull Long id, String firstName, String lastName, @NotBlank String email) {

    public CustomerProfile(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
