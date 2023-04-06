package com.uniTech.uniTechTask.service;

import com.uniTech.uniTechTask.model.CurrencyRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@CacheConfig(cacheNames = "currencyRates")
public class ExternalCurrencyRatesServiceImpl implements ExternalCurrencyRatesService {
    @Override
    @Cacheable("currencyRates")
    public List<CurrencyRate> getCurrencyRates() {
        log.info("Third party currency service is called");
        return List.of(new CurrencyRate("AZN", "USD", new BigDecimal("0.59")),
                new CurrencyRate("TL", "AZN", new BigDecimal("0.125")),
                new CurrencyRate("USD", "AZN", new BigDecimal("1.7")),
                new CurrencyRate("AZN", "TL", new BigDecimal("8")));
    }

    @CacheEvict(value = "currencyRates", allEntries = true)
    @Scheduled(fixedRateString = "${caching.currencyRatesTTL}")
    public void emptyCurrencyRatesCache() {
        log.info("emptying currency rates cache");
    }
}
