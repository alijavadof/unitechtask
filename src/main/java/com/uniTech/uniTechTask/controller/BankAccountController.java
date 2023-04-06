package com.uniTech.uniTechTask.controller;

import com.uniTech.uniTechTask.dto.request.BankAccountRequest;
import com.uniTech.uniTechTask.dto.request.TransferMoneyRequest;
import com.uniTech.uniTechTask.dto.ActiveAccount;
import com.uniTech.uniTechTask.dto.response.BankAccountResponse;
import com.uniTech.uniTechTask.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bank-accounts")
public class BankAccountController {
    private BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(Principal principal, @RequestBody BankAccountRequest bankAccountRequest) {
        BankAccountResponse response = bankAccountService.createAccount(principal.getName(), bankAccountRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllActiveAccountsOfUser(Principal principal) {
        List<ActiveAccount> accounts = bankAccountService.getActiveAccountsByPin(principal.getName());
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(Principal principal, @RequestBody TransferMoneyRequest request) {
        bankAccountService.transfer(principal.getName(), request);
        return new ResponseEntity<>("Successfully transferred.", HttpStatus.OK);
    }
}
