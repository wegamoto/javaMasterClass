package com.wewe.weweShop.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {

    public static String formatPrice(BigDecimal price) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("th", "TH"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(price);
    }
}

