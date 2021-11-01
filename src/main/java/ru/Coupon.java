package ru;

import java.math.BigDecimal;

public class Coupon {
    public int index;
    public String date;
    public String size;
    public BigDecimal profit;

    public Coupon(String index, String date, String size, String profit){
        this.index = Integer.parseInt(index);
        this.date = date;
        this.size = size;
        this.profit = new BigDecimal(profit);
    }


}
