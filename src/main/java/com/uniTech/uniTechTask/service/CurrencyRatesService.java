package com.uniTech.uniTechTask.service;

import com.uniTech.uniTechTask.model.CurrencyRate;

public interface CurrencyRatesService {
    CurrencyRate getCurrencyRate(String source, String target);
}
