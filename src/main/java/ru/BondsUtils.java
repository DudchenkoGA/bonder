package ru;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static ru.TextUtils.getNum;

public class BondsUtils {


    public static ArrayList<Bond> getBondsDetails(ArrayList<String> links) {

        ArrayList<Bond> bonds = new ArrayList<>();
        int i=1;
        for (String link : links){
            String response = given().get("https://smart-lab.ru" + link).andReturn().asString();
            Document html = Jsoup.parse(response);
            Elements tables = html.body().getElementsByClass("simple-little-table bond");
            Elements info = tables.get(0).getElementsByTag("td");
            if (tables.size()==1){
               continue;
            }
            Elements coupon_table = tables.get(1).getElementsByTag("tr");
            coupon_table.remove(0);
            if (coupon_table.size()<3){
                continue;
            }
//            System.out.println("trying to parse bond #" +i + " link: " + link);
//            if (i==48){
//                System.out.println("BLYAT");
//            }


            ArrayList<ArrayList<String>> good_coupon_table = new ArrayList<>();
            for (Element tr : coupon_table){
                ArrayList<String> td_list = new ArrayList<>();
                Elements td = tr.getElementsByTag("td");
                for (Element element : td){
                    td_list.add(element.text());
                }
                good_coupon_table.add(td_list);
            }


            ArrayList<Coupon> coupon_calendar = new ArrayList<>();

            for (ArrayList<String> tr : good_coupon_table){
                if (tr.get(3).matches(".*\\d.*")){
                    coupon_calendar.add(
                            new Coupon(
                                    getNum(tr.get(0)),
                                    tr.get(1),
                                    tr.get(2),
                                    getNum(tr.get(3))
                            )
                    );
                }else {
                    break;
                }
            }
            if (coupon_calendar.size()<3){
                continue;
            }

            if (info.get(11).text().matches(".*\\d.*")&&info.get(15).text().matches(".*\\d.*")){

                if (!info.get(17).text().contains(".")&&!info.get(55).text().equals("")){
                    bonds.add(
                        new Bond(info.get(1).text(),
                                info.get(3).text(),
                                LocalDate.now(),
                                info.get(55).text(),
                                info.get(17).text(),
                                info.get(9).text().matches(".*\\d.*")?info.get(9).text():info.get(11).text(),
                                info.get(27).text(),
                                info.get(15).text(),
                                getNum(info.get(23).text()),
                                getNum(info.get(21).text()),
                                info.get(51).text(),
                                coupon_calendar
                        )
                    );
                }
            }
            coupon_calendar.clear();
            i++;
        }
        return bonds;
    }

    public static Bond getBondDetails(String link) {

        Bond bond = null;
        String response = given().get(link).andReturn().asString();
        Document html = Jsoup.parse(response);
        Elements tables = html.body().getElementsByClass("simple-little-table bond");
        Elements info = tables.get(0).getElementsByTag("td");
        if (tables.size()==1){
            throw new NullPointerException("нет таблицы купонов");
        }
        Elements coupon_table = tables.get(1).getElementsByTag("tr");
        coupon_table.remove(0);

//            System.out.println("trying to parse bond #" +i + " link: " + link);
//            if (i==48){
//                System.out.println("BLYAT");
//            }


        ArrayList<ArrayList<String>> good_coupon_table = new ArrayList<>();
        for (Element tr : coupon_table){
            ArrayList<String> td_list = new ArrayList<>();
            Elements td = tr.getElementsByTag("td");
            for (Element element : td){
                td_list.add(element.text());
            }
            good_coupon_table.add(td_list);
        }


        ArrayList<Coupon> coupon_calendar = new ArrayList<>();

        for (ArrayList<String> tr : good_coupon_table){
            if (tr.get(3).matches(".*\\d.*")){
                coupon_calendar.add(
                        new Coupon(
                                getNum(tr.get(0)),
                                tr.get(1),
                                tr.get(2),
                                getNum(tr.get(3))
                        )
                );
            }else {
                break;
            }
        }
        if (coupon_calendar.size()<3){
            throw new NullPointerException("мало купонов");
        }

        if (info.get(11).text().matches(".*\\d.*")&&info.get(15).text().matches(".*\\d.*")){

            if (!info.get(17).text().contains(".")&&!info.get(55).text().equals("")){
                bond=new Bond(info.get(1).text(),
                              info.get(3).text(),
                              LocalDate.now(),
                              info.get(55).text(),
                              info.get(17).text(),
                              info.get(9).text().matches(".*\\d.*")?info.get(9).text():info.get(11).text(),
                              info.get(27).text(),
                              info.get(15).text(),
                              getNum(info.get(23).text()),
                              getNum(info.get(21).text()),
                              info.get(51).text(),
                              coupon_calendar
                        );

            }
        }

        return bond;
    }

}
