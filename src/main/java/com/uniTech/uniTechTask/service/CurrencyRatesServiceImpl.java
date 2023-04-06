package com.uniTech.uniTechTask.service;

import com.uniTech.uniTechTask.exception.CurrencyPairNotFoundException;
import com.uniTech.uniTechTask.model.CurrencyRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyRatesServiceImpl implements CurrencyRatesService {
    private ExternalCurrencyRatesService externalCurrencyRatesService;

    public CurrencyRatesServiceImpl(ExternalCurrencyRatesService externalCurrencyRatesService) {
        this.externalCurrencyRatesService = externalCurrencyRatesService;
    }

    @Override
    public CurrencyRate getCurrencyRate(String source, String target) {
        List<CurrencyRate> currencyRates = externalCurrencyRatesService.getCurrencyRates();
        return currencyRates.stream()
                .filter(cur -> cur.getSourceCurrency().equals(source)
                        && cur.getTargetCurrency().equals(target))
                .findFirst()
                .orElseThrow(() -> new CurrencyPairNotFoundException("Currency pair not found"));
    }
}
