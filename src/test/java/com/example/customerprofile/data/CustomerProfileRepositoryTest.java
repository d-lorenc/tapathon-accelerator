package com.example.customerprofile.data;

import com.example.customerprofile.domain.NewCustomerProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.customerprofile.domain.TestData.testNewCustomerProfile;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CustomerProfileRepositoryTest {

    @Autowired
    private CustomerProfileRepository subject;

    @Test
	void shouldPersistCustomerProfile() {
		NewCustomerProfile newCustomerProfile = testNewCustomerProfile();

		var customerProfile = subject.create(newCustomerProfile);
		var actual = subject.findById(customerProfile.id());

		var actualEntity = actual.get();
		assertThat(actualEntity.firstName()).isEqualTo(customerProfile.firstName());
		assertThat(actualEntity.lastName()).isEqualTo(customerProfile.lastName());
		assertThat(actualEntity.email()).isEqualTo(customerProfile.email());
	}
}
