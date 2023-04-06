package com.uniTech.uniTechTask.service;

import com.uniTech.uniTechTask.model.CurrencyRate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface ExternalCurrencyRatesService {
    List<CurrencyRate> getCurrencyRates();
}
