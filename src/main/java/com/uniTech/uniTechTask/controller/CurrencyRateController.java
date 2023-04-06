package com.uniTech.uniTechTask.controller;

import com.uniTech.uniTechTask.model.CurrencyRate;
import com.uniTech.uniTechTask.service.CurrencyRatesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/currency-rate")
public class CurrencyRateController {
    private CurrencyRatesService currencyRatesService;

    public CurrencyRateController(CurrencyRatesService currencyRatesService) {
        this.currencyRatesService = currencyRatesService;
    }

    @GetMapping
    public ResponseEntity<?> getCurrencyRate(@RequestParam String source, @RequestParam String target) {
        CurrencyRate currencyRate = currencyRatesService.getCurrencyRate(source, target);
        return new ResponseEntity<>(currencyRate, HttpStatus.OK);
    }
}
