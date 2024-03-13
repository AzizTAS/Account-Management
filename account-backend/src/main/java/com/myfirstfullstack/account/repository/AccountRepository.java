package com.myfirstfullstack.account.repository;

import com.myfirstfullstack.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}
