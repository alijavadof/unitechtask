package com.uniTech.uniTechTask.service;

import com.uniTech.uniTechTask.dto.response.BankAccountResponse;
import com.uniTech.uniTechTask.exception.*;
import com.uniTech.uniTechTask.model.BankAccount;
import com.uniTech.uniTechTask.model.Users;
import com.uniTech.uniTechTask.repository.BankAccountRepository;
import com.uniTech.uniTechTask.dto.request.BankAccountRequest;
import com.uniTech.uniTechTask.dto.request.TransferMoneyRequest;
import com.uniTech.uniTechTask.dto.ActiveAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountRepository bankAccountRepository;
    private UserServiceImpl userServiceImpl;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserServiceImpl userServiceImpl) {
        this.bankAccountRepository = bankAccountRepository;
        this.userServiceImpl = userServiceImpl;
    }

    public BankAccountResponse createAccount(String pin, BankAccountRequest bankAccountRequest) {
        Users user = userServiceImpl.getUserByPin(pin)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        BankAccount bankAccount = BankAccount.builder()
                .accountNumber(UUID.randomUUID().toString().substring(0, 8))
                .user(user)
                .balance(bankAccountRequest.getBalance())
                .createdDate(new Date())
                .isActive(true)
                .build();

        bankAccountRepository.save(bankAccount);

        return BankAccountResponse.builder()
                .accountNumber(bankAccount.getAccountNumber())
                .balance(bankAccount.getBalance())
                .createdDate(bankAccount.getCreatedDate())
                .build();
    }

    @Override
    public List<ActiveAccount> getActiveAccountsByPin(String pin) {
        return bankAccountRepository.findAllAccountByPin(pin);
    }

    @Transactional
    public void transfer(String pin, TransferMoneyRequest request) {
        if (isSameAccount(request.getFromAccountNumber(), request.getToAccountNumber())) {
            throw new TheSameAccountException("Can not transfer between the same accounts.");
        }

        var fromAccount = getAccount(pin, request.getFromAccountNumber())
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found in your accounts."));

        var toAccount = getAccount(request.getToAccountNumber())
                .orElseThrow(() -> new BankAccountNotFoundException("Can not send money to non-existent account."));

        if (!toAccount.isActive()) {
            throw new NotActiveAccountException("Can not send money to deactive account.");
        }

        if (!hasEnoughMoney(fromAccount, request.getMoney())) {
            throw new NotEnoughBalanceException("Not enough balance for transfer.");
        }

        toAccount.setBalance(toAccount.getBalance().add(request.getMoney()));
        bankAccountRepository.save(toAccount);

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getMoney()));
        bankAccountRepository.save(fromAccount);
    }

    private boolean isSameAccount(String fromAccountNumber, String toAccountNumber) {
        return StringUtils.hasText(fromAccountNumber) && StringUtils.hasText(toAccountNumber) && fromAccountNumber.equals(toAccountNumber);
    }

    private boolean hasEnoughMoney(BankAccount bankAccount, BigDecimal balance) {
        int compare = bankAccount.getBalance().compareTo(balance);
        return compare == 0 || compare == 1;
    }

    private Optional<BankAccount> getAccount(String toAccountNumber) {
        Optional<BankAccount> bankAccount = bankAccountRepository.findBankAccountByAccountNumber(toAccountNumber);
        return bankAccount;
    }

    private Optional<BankAccount> getAccount(String pin, String accountNumber) {
        Optional<Users> user = userServiceImpl.getUserByPin(pin);
        Optional<BankAccount> bankAccount = bankAccountRepository.findBankAccountByUserIdAndAccountNumber(user.get().getId(), accountNumber);
        return bankAccount;
    }
}
