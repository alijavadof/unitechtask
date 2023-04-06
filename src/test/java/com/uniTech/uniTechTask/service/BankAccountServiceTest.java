package com.uniTech.uniTechTask.service;

import com.uniTech.uniTechTask.dto.ActiveAccount;
import com.uniTech.uniTechTask.dto.ActiveAccountDto;
import com.uniTech.uniTechTask.exception.*;
import com.uniTech.uniTechTask.model.BankAccount;
import com.uniTech.uniTechTask.model.Users;
import com.uniTech.uniTechTask.repository.BankAccountRepository;
import com.uniTech.uniTechTask.dto.request.BankAccountRequest;
import com.uniTech.uniTechTask.dto.request.TransferMoneyRequest;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
class BankAccountServiceTest {
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount() {
        var pin = "qwerty";
        var user = Users.builder().pin(pin).build();
        var bankAccountRequest = BankAccountRequest.builder()
                .balance(new BigDecimal(5000))
                .build();
        when(userService.getUserByPin(pin)).thenReturn(Optional.of(user));

        var bankAccountResponse = bankAccountService.createAccount(pin, bankAccountRequest);
        verify(bankAccountRepository, times(1)).save(any());
        assertTrue(StringUtils.hasText(bankAccountResponse.getAccountNumber()));
    }

    @Test
    void createAccount_shouldFail_when_userNotFound() {
        var pin = "qwerty";
        var bankAccountRequest = BankAccountRequest.builder()
                .balance(new BigDecimal(5000))
                .build();

        when(userService.getUserByPin(pin)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> bankAccountService.createAccount(pin, bankAccountRequest),
                "User not found"
        );
    }

    @Test
    void getActiveAccountByPin() {
        var pin = "qwerty";
        List<ActiveAccount> activeAccounts = List.of(ActiveAccountDto.builder().accountNumber("abc")
                .createdDate(new Date())
                .balance(new BigDecimal(500))
                .build(), ActiveAccountDto.builder().accountNumber("abd")
                .createdDate(new Date())
                .balance(new BigDecimal(800))
                .build());
        when(bankAccountRepository.findAllAccountByPin(eq(pin))).thenReturn(activeAccounts);

        var bankAccountLs = bankAccountService.getActiveAccountsByPin(pin);

        assertEquals(bankAccountLs.size(), 2);
    }

    @Test
    void transfer() {
        var pin = "qwerty";
        var accountFrom = "account2";
        var accountTo = "account1";
        var user = Users.builder()
                .id(1)
                .pin(pin)
                .build();
        var bankAccountFrom = BankAccount.builder()
                .balance(new BigDecimal(50000))
                .accountNumber(accountFrom)
                .isActive(true)
                .build();
        var bankAccountTo = BankAccount.builder()
                .balance(new BigDecimal(50000))
                .accountNumber(accountTo)
                .isActive(true)
                .build();
        var request = TransferMoneyRequest.builder()
                .fromAccountNumber(accountFrom)
                .toAccountNumber(accountTo)
                .money(new BigDecimal(5000))
                .build();

        when(userService.getUserByPin(pin)).thenReturn(Optional.of(user));

        when(bankAccountRepository.findBankAccountByAccountNumber(eq(accountFrom)))
                .thenReturn(Optional.of(bankAccountFrom));

        when(bankAccountRepository.findBankAccountByAccountNumber(eq(accountTo)))
                .thenReturn(Optional.of(bankAccountTo));

        when(bankAccountRepository.findBankAccountByUserIdAndAccountNumber(user.getId(), accountFrom))
                .thenReturn(Optional.of(bankAccountFrom));

        bankAccountService.transfer(pin, request);

        verify(bankAccountRepository, times(2)).save(any());
    }

    @Test
    void transfer_shouldFail_whenAccounts_theSame() {
        var pin = "qwerty";
        var accountFrom = "account1";
        var accountTo = "account1";
        var user = Users.builder()
                .id(1)
                .pin(pin)
                .build();
        var bankAccountFrom = BankAccount.builder()
                .balance(new BigDecimal(50000))
                .accountNumber(accountFrom)
                .isActive(true)
                .build();
        var bankAccountTo = BankAccount.builder()
                .balance(new BigDecimal(50000))
                .accountNumber(accountTo)
                .isActive(true)
                .build();
        var request = TransferMoneyRequest.builder()
                .fromAccountNumber(accountFrom)
                .toAccountNumber(accountTo)
                .money(new BigDecimal(5000))
                .build();

        when(userService.getUserByPin(pin)).thenReturn(Optional.of(user));

        when(bankAccountRepository.findBankAccountByAccountNumber(eq(accountFrom)))
                .thenReturn(Optional.of(bankAccountFrom));

        when(bankAccountRepository.findBankAccountByAccountNumber(eq(accountTo)))
                .thenReturn(Optional.of(bankAccountTo));

        when(bankAccountRepository.findBankAccountByUserIdAndAccountNumber(user.getId(), accountFrom))
                .thenReturn(Optional.of(bankAccountFrom));

        var exception = assertThrows(TheSameAccountException.class,
                () -> bankAccountService.transfer(pin, request));
        assertEquals("Can not transfer between the same accounts.", exception.getMessage());
    }

    @Test
    void transfer_shouldFail_when_recipientAccount_isDeactive() {
        var pin = "qwerty";
        var accountFrom = "account2";
        var accountTo = "account1";
        var user = Users.builder()
                .id(1)
                .pin(pin)
                .build();
        var bankAccountFrom = BankAccount.builder()
                .balance(new BigDecimal(50000))
                .accountNumber(accountFrom)
                .isActive(true)
                .build();
        var bankAccountTo = BankAccount.builder()
                .balance(new BigDecimal(50000))
                .accountNumber(accountTo)
                .isActive(false)
                .build();
        var request = TransferMoneyRequest.builder()
                .fromAccountNumber(accountFrom)
                .toAccountNumber(accountTo)
                .money(new BigDecimal(5000))
                .build();

        when(userService.getUserByPin(pin)).thenReturn(Optional.of(user));

        when(bankAccountRepository.findBankAccountByAccountNumber(eq(accountFrom)))
                .thenReturn(Optional.of(bankAccountFrom));

        when(bankAccountRepository.findBankAccountByAccountNumber(eq(accountTo)))
                .thenReturn(Optional.of(bankAccountTo));

        when(bankAccountRepository.findBankAccountByUserIdAndAccountNumber(user.getId(), accountFrom))
                .thenReturn(Optional.of(bankAccountFrom));

        var exception = assertThrows(NotActiveAccountException.class,
                () -> bankAccountService.transfer(pin, request));
        assertEquals("Can not send money to deactive account.", exception.getMessage());
    }

    @Test
    void transfer_shouldFail_when_recipientAccount_isNonExists() {
        var pin = "qwerty";
        var accountFrom = "account2";
        var accountTo = "account1";
        var user = Users.builder()
                .id(1)
                .pin(pin)
                .build();
        var bankAccountFrom = BankAccount.builder()
                .balance(new BigDecimal(50000))
                .accountNumber(accountFrom)
                .isActive(true)
                .build();
        var request = TransferMoneyRequest.builder()
                .fromAccountNumber(accountFrom)
                .toAccountNumber(accountTo)
                .money(new BigDecimal(5000))
                .build();

        when(userService.getUserByPin(pin)).thenReturn(Optional.of(user));

        when(bankAccountRepository.findBankAccountByAccountNumber(eq(accountFrom)))
                .thenReturn(Optional.of(bankAccountFrom));

        when(bankAccountRepository.findBankAccountByAccountNumber(eq(accountTo)))
                .thenReturn(Optional.empty());

        when(bankAccountRepository.findBankAccountByUserIdAndAccountNumber(user.getId(), accountFrom))
                .thenReturn(Optional.of(bankAccountFrom));

        var exception = assertThrows(BankAccountNotFoundException.class,
                () -> bankAccountService.transfer(pin, request));
        assertEquals("Can not send money to non-existent account.", exception.getMessage());
    }

    @Test
    void transfer_shouldFail_when_notEnoughBalance() {
        var pin = "qwerty";
        var accountFrom = "account2";
        var accountTo = "account1";
        var user = Users.builder()
                .id(1)
                .pin(pin)
                .build();
        var bankAccountFrom = BankAccount.builder()
                .balance(new BigDecimal(1))
                .accountNumber(accountFrom)
                .isActive(true)
                .build();
        var bankAccountTo = BankAccount.builder()
                .balance(new BigDecimal(50000))
                .accountNumber(accountTo)
                .isActive(true)
                .build();
        var request = TransferMoneyRequest.builder()
                .fromAccountNumber(accountFrom)
                .toAccountNumber(accountTo)
                .money(new BigDecimal(5000))
                .build();

        when(userService.getUserByPin(pin)).thenReturn(Optional.of(user));

        when(bankAccountRepository.findBankAccountByAccountNumber(eq(accountFrom)))
                .thenReturn(Optional.of(bankAccountFrom));

        when(bankAccountRepository.findBankAccountByAccountNumber(eq(accountTo)))
                .thenReturn(Optional.of(bankAccountTo));

        when(bankAccountRepository.findBankAccountByUserIdAndAccountNumber(user.getId(), accountFrom))
                .thenReturn(Optional.of(bankAccountFrom));

        var exception = assertThrows(NotEnoughBalanceException.class,
                () -> bankAccountService.transfer(pin, request)
        );
        assertEquals("Not enough balance for transfer.", exception.getMessage());
    }
}