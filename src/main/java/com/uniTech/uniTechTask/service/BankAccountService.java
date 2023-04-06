package com.uniTech.uniTechTask.service;

import com.uniTech.uniTechTask.dto.request.BankAccountRequest;
import com.uniTech.uniTechTask.dto.request.TransferMoneyRequest;
import com.uniTech.uniTechTask.dto.ActiveAccount;
import com.uniTech.uniTechTask.dto.response.BankAccountResponse;

import java.math.BigDecimal;
import java.util.List;

public interface BankAccountService {
    BankAccountResponse createAccount(String pin, BankAccountRequest bankAccountRequest);

    List<ActiveAccount> getActiveAccountsByPin(String pin);

    void transfer(String pin, TransferMoneyRequest request);
}
