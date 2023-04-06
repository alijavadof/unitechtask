package com.uniTech.uniTechTask.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class ActiveAccountDto implements ActiveAccount{
    private String accountNumber;
    private BigDecimal balance;
    private Date createdDate;
}
