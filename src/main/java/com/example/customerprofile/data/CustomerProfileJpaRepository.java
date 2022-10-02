package com.example.customerprofile.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CustomerProfileJpaRepository extends JpaRepository<CustomerProfileEntity, Long> {
}
