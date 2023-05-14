package com.cqcj.util;

import com.cqcj.pojo.Commodity;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @program: Jsoup-Echarts
 * @description:
 * @author: 王炸！！
 * @create: 2023-05-14 14:04
 **/
public class JsoupUtil {
    public static ArrayList<Commodity> JDgetData(Document document){
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
            arrCommodity.add(commodity);
        });
        return arrCommodity;
    }
}
