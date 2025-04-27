package com.wewe.weweShop.util;

import java.text.DecimalFormat;

public class NumberToWords {

    private static final String[] NUMBERS = {
            "ศูนย์", "หนึ่ง", "สอง", "สาม", "สี่", "ห้า", "หก", "เจ็ด", "แปด", "เก้า"
    };

    private static final String[] THOUSANDS = {
            "", "พัน", "ล้าน", "พันล้าน", "ล้านล้าน"
    };

    private static final String[] THAI_CURRENCY = {"บาท", "สตางค์"};

    public static String convertToThaiCurrency(double amount) {
        // แปลงจำนวนเงินเป็น String
        StringBuilder result = new StringBuilder();
        long intPart = (long) amount;
        int decimalPart = (int) ((amount - intPart) * 100);

        // แปลงจำนวนเต็มเป็นคำ
        result.append(convertNumberToWords(intPart)).append(" ").append(THAI_CURRENCY[0]);

        // แปลงจำนวนน้อยกว่า 1 บาท (สตางค์)
        if (decimalPart > 0) {
            result.append(" ").append(convertNumberToWords(decimalPart)).append(" ").append(THAI_CURRENCY[1]);
        }

        return result.toString();
    }

    private static String convertNumberToWords(long number) {
        if (number == 0) {
            return NUMBERS[0];
        }

        StringBuilder words = new StringBuilder();
        int unitIndex = 0;
        while (number > 0) {
            int part = (int) (number % 1000);
            if (part > 0) {
                words.insert(0, convertThreeDigitNumber(part) + THOUSANDS[unitIndex] + " ");
            }
            number /= 1000;
            unitIndex++;
        }

        return words.toString().trim();
    }

    private static String convertThreeDigitNumber(int number) {
        StringBuilder result = new StringBuilder();
        if (number >= 100) {
            result.append(NUMBERS[number / 100]).append("ร้อย ");
            number %= 100;
        }
        if (number >= 20) {
            result.append(NUMBERS[number / 10]).append("สิบ ");
            number %= 10;
        }
        if (number > 0) {
            result.append(NUMBERS[number]);
        }

        return result.toString();
    }

    public static void main(String[] args) {
        // ทดสอบการแปลงเลขเป็นคำ
        System.out.println(convertToThaiCurrency(1234.50)); // "หนึ่งพันสองร้อยสามสิบสี่บาทห้าสิบสตางค์"
        System.out.println(convertToThaiCurrency(100.10));  // "หนึ่งร้อยบาทสิบสตางค์"
    }
}

