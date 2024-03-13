package com.myfirstfullstack.account.repository;


import com.myfirstfullstack.account.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}