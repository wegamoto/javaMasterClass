package com.wewe.weweShop.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Service
public class CurrencyService {

    public String formatCurrency(BigDecimal amount) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(amount);
    }

}
