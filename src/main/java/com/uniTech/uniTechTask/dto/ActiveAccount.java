package com.uniTech.uniTechTask.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface ActiveAccount {
    String getAccountNumber();
    BigDecimal getBalance();
    Date getCreatedDate();
}
