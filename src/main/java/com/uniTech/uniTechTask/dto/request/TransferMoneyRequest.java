package com.uniTech.uniTechTask.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferMoneyRequest {
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal money;
}
