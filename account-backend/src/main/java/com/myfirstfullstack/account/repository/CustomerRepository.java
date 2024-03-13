package com.myfirstfullstack.account.repository;

import com.myfirstfullstack.account.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}