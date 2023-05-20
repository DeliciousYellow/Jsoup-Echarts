package com.cqcj.util;

import com.cqcj.pojo.Commodity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.naming.Name;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @program: Jsoup-Echarts
 * @description:
 * @author: 王炸！！
 * @create: 2023-05-14 14:04
 **/
public class JsoupUtil {
    public static ArrayList<Commodity> JDgetData(Document document,boolean isdetail){
        //拿到每个商品信息的父标签
        Elements Arrelement = document.body().getElementsByClass("gl-i-wrap");
        ArrayList<Commodity> arrCommodity = new ArrayList<>();
        Arrelement.forEach(e -> {
            //解析图片URL
            Elements imgElements = e.select("div.p-img").select("img");
            String imgUrl = imgElements.attr("data-lazy-img");
            String Url = "https:" + imgUrl;
            //解析商品价格
            Elements priceElements = e.select("div.p-price").select("i");
            String StringPrice = priceElements.text();
            BigDecimal Price = BigDecimal.valueOf(Double.parseDouble(StringPrice));
            //解析商品名字
            Elements nameElements = e.select("div.p-name").select("a").select("em");
            String Name = nameElements.text();
            //封装商品类
            Commodity commodity = new Commodity();
            commodity.setName(Name);
            commodity.setPrice(Price);
            commodity.setUrl(Url);
            //详情页超链接
            String href = e.select("div.p-name").select("a").attr("href");
            //商品内部ID
            String substring = href.substring(14);
            int end = substring.indexOf(".");
            String Code = substring.substring(0, end);
            String detailUrl = "https:" + href;
            //封装商品ID
            commodity.setCode(Code);
            //小心反爬,是否详细查询
            if (isdetail){
                JsoupUtil.JDgetDetial(detailUrl,commodity);
            }
            //添加到集合中
            arrCommodity.add(commodity);
        });
        return arrCommodity;
    }
    public static Commodity JDgetDetial(String detailUrl,Commodity commodity){
        Document document = null;
        try {
            document = Jsoup.connect(detailUrl).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements element = document.body().getElementsByClass("p-parameter");

        Elements select = element.select("ul.p-parameter-list");
        Elements eq0 = select.eq(0);
        String brand = eq0.select("li").attr("title");
        commodity.setBrand(brand);

//        Elements eq1 = select.eq(1);
//        String Name = eq1.select("li").attr("title");
//        if (Name!=null){
//            commodity.setName(Name);
//        }

        Elements evaluate = document.body().getElementsByClass("tab-main small");
        Elements select1 = evaluate.select("a");
//        .select("a").select("em").text()
        return commodity;
    }
}
