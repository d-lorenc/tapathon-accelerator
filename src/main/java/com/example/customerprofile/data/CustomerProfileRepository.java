package com.example.customerprofile.data;

import com.example.customerprofile.domain.CustomerProfile;
import com.example.customerprofile.domain.NewCustomerProfile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerProfileRepository {

    private final CustomerProfileJpaRepository jpaRepository;

    public CustomerProfileRepository(CustomerProfileJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public CustomerProfile create(NewCustomerProfile newCustomerProfile) {
        var savedEntity = jpaRepository.save(toEntity(newCustomerProfile));
        return fromEntity(savedEntity);
    }

    public Optional<CustomerProfile> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::fromEntity);
    }

    private CustomerProfileEntity toEntity(NewCustomerProfile newCustomerProfile) {
        var entity = new CustomerProfileEntity();
        entity.setFirstName(newCustomerProfile.firstName());
        entity.setLastName(newCustomerProfile.lastName());
        entity.setEmail(newCustomerProfile.email());
        return entity;
    }

    private CustomerProfile fromEntity(CustomerProfileEntity entity) {
        return new CustomerProfile(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail());
    }
}
