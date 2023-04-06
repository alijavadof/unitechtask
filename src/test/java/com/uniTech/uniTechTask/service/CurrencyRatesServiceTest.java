package com.uniTech.uniTechTask.service;

import com.uniTech.uniTechTask.model.CurrencyRate;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
class CurrencyRatesServiceTest {
    @Mock
    private ExternalCurrencyRatesService externalCurrencyRatesService;
    @InjectMocks
    private CurrencyRatesServiceImpl currencyRatesService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrencyRate() {
        String target = "AZN", source = "USD";
        var currencyRateList = List.of(new CurrencyRate("AZN", "USD", new BigDecimal("0.59")),
                new CurrencyRate("TL", "AZN", new BigDecimal("0.125")),
                new CurrencyRate("USD", "AZN", new BigDecimal("1.7")),
                new CurrencyRate("AZN", "TL", new BigDecimal("8")));
        when(externalCurrencyRatesService.getCurrencyRates()).thenReturn(currencyRateList);

        var currentRate =  currencyRatesService.getCurrencyRate(source, target);

        assertEquals(new BigDecimal("1.7"), currentRate.getRate());
    }
}