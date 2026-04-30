package com.aziztas.account.service;

import com.aziztas.account.TestSupport;
import com.aziztas.account.dto.AccountDto;
import com.aziztas.account.dto.CreateAccountRequest;
import com.aziztas.account.dto.converter.AccountDtoConverter;
import com.aziztas.account.exception.CustomerNotFoundException;
import com.aziztas.account.model.Account;
import com.aziztas.account.model.Customer;
import com.aziztas.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

public class AccountServiceTest extends TestSupport {

    private AccountRepository accountRepository;
    private CustomerService customerService;
    private AccountDtoConverter converter;
    private Clock clock;

    private AccountService service;

    @BeforeEach
    public void setUp() {
        accountRepository = mock(AccountRepository.class);
        customerService = mock(CustomerService.class);
        converter = mock(AccountDtoConverter.class);
        clock = Clock.fixed(Instant.parse("2021-06-15T10:15:30Z"), Clock.systemDefaultZone().getZone());
        service = new AccountService(accountRepository, customerService, converter, clock);
    }

    @Test
    public void testCreateAccount_whenInitialCreditIsZero_shouldCreateAccountWithoutTransaction() {
        Customer customer = generateCustomer();
        CreateAccountRequest request = generateCreateAccountRequest(0);
        Account account = new Account(customer, BigDecimal.ZERO, getLocalDateTime());
        AccountDto accountDto = new AccountDto("account-id", BigDecimal.ZERO, getLocalDateTime(), null, Set.of());

        Mockito.when(customerService.findCustomerById("customer-id")).thenReturn(customer);
        Mockito.when(accountRepository.save(any(Account.class))).thenReturn(account);
        Mockito.when(converter.convert(account)).thenReturn(accountDto);

        AccountDto result = service.createAccount(request);

        assertEquals(result, accountDto);
    }

    @Test
    public void testCreateAccount_whenInitialCreditIsGreaterThanZero_shouldCreateAccountWithTransaction() {
        Customer customer = generateCustomer();
        CreateAccountRequest request = generateCreateAccountRequest(100);
        Account account = new Account(customer, new BigDecimal(100), getLocalDateTime());
        AccountDto accountDto = new AccountDto("account-id", new BigDecimal(100), getLocalDateTime(), null, Set.of());

        Mockito.when(customerService.findCustomerById("customer-id")).thenReturn(customer);
        Mockito.when(accountRepository.save(any(Account.class))).thenReturn(account);
        Mockito.when(converter.convert(account)).thenReturn(accountDto);

        AccountDto result = service.createAccount(request);

        assertEquals(result, accountDto);
    }

    @Test
    public void testCreateAccount_whenCustomerIdDoesNotExist_shouldThrowCustomerNotFoundException() {
        CreateAccountRequest request = generateCreateAccountRequest(0);

        Mockito.when(customerService.findCustomerById("customer-id"))
                .thenThrow(new CustomerNotFoundException("customer-id"));

        assertThrows(CustomerNotFoundException.class, () -> service.createAccount(request));

        Mockito.verify(accountRepository, never()).save(any());
    }
}
