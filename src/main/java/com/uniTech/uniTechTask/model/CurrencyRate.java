package com.uniTech.uniTechTask.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRate {
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal rate;
}
