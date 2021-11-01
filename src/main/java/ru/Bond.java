package ru;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static ru.TextUtils.stringToDate;

public class Bond {
    String name;
    String code;
    LocalDate buy_date;
    BigDecimal price;
    BigDecimal nominal;
    LocalDate end_date;
    LocalDate end_date_eff;
    Integer coupon_period;
    LocalDate nearest_coupon_date;
    BigDecimal coupon;
    BigDecimal coupon_percent;
    BigDecimal accrued_interest;
    public Integer listing;
    public ArrayList<Coupon> coupon_calendar;

    public Bond(String name, String code, LocalDate buy_date, String price, String nominal, String end_date, String coupon_period,
                String nearest_coupon_date, String coupon, String coupon_percent, String listing, ArrayList<Coupon> coupon_calendar) {
        this.name = name;
        this.code = code;
        this.buy_date = buy_date;
        this.price = new BigDecimal(price);
        this.nominal = new BigDecimal(nominal);
        this.end_date = stringToDate(coupon_calendar.size()>0?coupon_calendar.get(coupon_calendar.size()-1).date:end_date,
                "dd-MM-yyyy");
        this.coupon_period = Integer.parseInt(coupon_period)==0?10000:Integer.parseInt(coupon_period);
        this.nearest_coupon_date = stringToDate(nearest_coupon_date,"dd-MM-yyyy");
        this.coupon = new BigDecimal(coupon);
        this.coupon_percent = new BigDecimal(coupon_percent);
        this.accrued_interest = this.coupon.setScale(10).divide(BigDecimal.valueOf(this.coupon_period), BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(this.coupon_period - ChronoUnit.DAYS.between(this.buy_date,this.nearest_coupon_date)))
                .setScale(2,BigDecimal.ROUND_HALF_UP);
        this.listing = Integer.valueOf(listing);
        this.coupon_calendar = coupon_calendar;
    }

    public String getProfit(){

        for (Coupon coupon : this.coupon_calendar){
            if (coupon.profit.compareTo(this.coupon_percent)<0){
                this.end_date = stringToDate(this.coupon_calendar.get(coupon.index-2).date,"dd-MM-yyyy");
                break;
            }
        }

        long count_of_coupons = ChronoUnit.DAYS.between(this.nearest_coupon_date, this.end_date)/this.coupon_period+1;
        BigDecimal buy = this.nominal.multiply(this.price).divide(BigDecimal.valueOf(100)).add(this.accrued_interest)
                .multiply(BigDecimal.valueOf(100.05).divide(BigDecimal.valueOf(100))).setScale(10);
        BigDecimal sell = this.nominal.add(this.coupon.multiply(BigDecimal.valueOf(count_of_coupons))
                .multiply(BigDecimal.valueOf(0.87))).setScale(10);
        BigDecimal profit = (sell.divide(buy, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.valueOf(1)))
                .divide(BigDecimal.valueOf(ChronoUnit.DAYS.between(this.buy_date, this.end_date)), BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(365)).multiply(BigDecimal.valueOf(100)).setScale(4, BigDecimal.ROUND_HALF_UP);

        return profit.toString();
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public LocalDate getBuy_date() {
        return this.buy_date;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getNominal() {
        return this.nominal;
    }

    public LocalDate getEnd_date() {
        return this.end_date;
    }

    public Integer getCoupon_period() {
        return this.coupon_period;
    }

    public LocalDate getNearest_coupon_date() {
        return this.nearest_coupon_date;
    }

    public BigDecimal getCoupon() {
        return this.coupon;
    }

    public BigDecimal getAccrued_interest() {
        return this.accrued_interest;
    }

    public Integer getListing() {
        return this.listing;
    }

}
