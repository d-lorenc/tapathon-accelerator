package com.example.customerprofile.domain;

import com.example.customerprofile.data.CustomerProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.customerprofile.domain.TestData.testCustomerProfile;
import static com.example.customerprofile.domain.TestData.testNewCustomerProfile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerProfileServiceTest {

    @Mock
    private CustomerProfileRepository repository;

    @InjectMocks
    private CustomerProfileService subject;

    @Test
    void shouldDelegateToRepositoryToPersistProfile() {
        NewCustomerProfile newCustomerProfile = testNewCustomerProfile();
        CustomerProfile customerProfile = testCustomerProfile();
        when(repository.create(any())).thenReturn(customerProfile);

        var result = subject.create(newCustomerProfile);

        assertThat(result).isSameAs(customerProfile);
        verify(repository).create(newCustomerProfile);
    }

    @Test
    void shouldDelegateToRepositoryToRetrieveProfile() {
        var optionalCustomerProfile = Optional.of(testCustomerProfile());
        when(repository.findById(any())).thenReturn(optionalCustomerProfile);

        var result = subject.getById(123L);

        assertThat(result).isSameAs(optionalCustomerProfile);
        verify(repository).findById(123L);
    }
}
