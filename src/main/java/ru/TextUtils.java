package ru;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Attachment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TextUtils {


    /**
     * Взять из названия продукта последние цифры пана
     *
     * @param number
     * @return
     */
    public static String get4LastProductNumber(String number) {
        return number.substring(number.length() - 4);
    }


    /**
     * Получить символ валюты
     *
     * @param currency
     * @return
     */
    public static String getCurrencyCode(String currency) {
        switch (currency) {
            case "RUR":
                return "\u20BD";
            case "USD":
                return "\u0024";
            case "EUR":
                return "\u20AC";
        }
        return null;
    }

    /**
     * Получить название валюты
     *
     * @param currency
     * @return
     */
    public static String getCurrencyName(String currency) {
        switch (currency) {
            case "RUR":
                return "Poccийские рубли";
            case "USD":
                return "Евро";
            case "EUR":
                return "Доллары США";
        }
        return null;
    }

    public static String deleteSpacesAndSymbols(String text) {
        return text.replaceAll("\\s+", "");
    }

    public static String formatResponse(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Attachment(value = "{title}", type = "text/plain")
    public static String attachText(String title, String text) {
        return text;
    }


    public static BigDecimal getBigDecimalWithoutFractionalNulls(BigDecimal amount) {
        BigDecimal scaleAmount = amount.setScale(0, RoundingMode.HALF_UP);
        if (amount.compareTo(scaleAmount) == 0) {
            return scaleAmount;
        }
        return amount;
    }


    public static String getAmountWithSpaces(BigDecimal amount) {
        String amountToString = amount.setScale(2, RoundingMode.HALF_UP).toString();
        String replacedString = amountToString.substring(amountToString.length() - 6);
        return amountToString.replace(replacedString, " " + replacedString);
    }

    public static LocalDate stringToDate(String date, String income_pattern){
//        System.out.print(date+" ");
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(income_pattern));
        }catch (DateTimeParseException e){
            date = "01-01-1970";
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(income_pattern));
        }

    }

    public static String transformStringDate(String date, String income_pattern, String outcome_pattern){
        LocalDate localDate = stringToDate(date,income_pattern);
        return localDate.format(DateTimeFormatter.ofPattern(outcome_pattern).withZone(ZoneId.systemDefault()));
    }

    public static String getNum(String str){
        return str.replaceAll("[^0-9.]+","");
    }
}
