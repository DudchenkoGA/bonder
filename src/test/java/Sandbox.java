import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;

import ru.Bond;

import java.util.*;

import static io.restassured.RestAssured.given;
import static ru.BondsUtils.getBondDetails;
import static ru.BondsUtils.getBondsDetails;


public class Sandbox  {

    @Test
    public void prost(){
        ArrayList<String> bond_links = new ArrayList<>();

//        String response = given().get("https://smart-lab.ru/q/bonds/order_by_yield_last/desc/").andReturn().asString();
//        String response = given().get("https://smart-lab.ru/q/eurobonds/order_by_yield_last/desc/").andReturn().asString();
        String response = given().get("https://smart-lab.ru/q/ofz/order_by_yield_last/desc/").andReturn().asString();
//        String response = given().get("https://smart-lab.ru/q/subfed/order_by_yield_last/desc/").andReturn().asString();

        Document html = Jsoup.parse(response);
        Elements tr = html.body().getElementsByTag("tr");
        tr.remove(0);
        for(Element element : tr){
            bond_links.add(element.getElementsByTag("td").get(2).children().attr("href"));
        }

//        response = given().get("https://smart-lab.ru/q/bonds/order_by_year_yield/desc/page2/").andReturn().asString();
//        html = Jsoup.parse(response);
//        tr = html.body().getElementsByTag("tr");
//        tr.remove(0);
//        for(Element element : tr){
//            bond_links.add(element.getElementsByTag("td").get(2).children().attr("href"));
//        }
//
//        response = given().get("https://smart-lab.ru/q/bonds/order_by_year_yield/desc/page3/").andReturn().asString();
//        html = Jsoup.parse(response);
//        tr = html.body().getElementsByTag("tr");
//        tr.remove(0);
//        for(Element element : tr){
//            bond_links.add(element.getElementsByTag("td").get(2).children().attr("href"));
//        }

        ArrayList<Bond> bonds = getBondsDetails(bond_links);
        bonds.removeIf(a->!(a.listing==1));

        Map result = new HashMap();
        bonds.forEach(a->result.put(a.getName() + ": " + a.getCode(), a.getProfit()));

        result.entrySet().stream().sorted(Map.Entry.comparingByValue().reversed()).limit(35).forEach(System.out::println);

    }


    @Test
    public void prost2(){
        String link = "https://smart-lab.ru/q/bonds/RU000A103JR3/";
        Bond bond = getBondDetails(link);
        System.out.println(bond.getName() + ": " + bond.getCode() +" "+ bond.getProfit());
    }

}
