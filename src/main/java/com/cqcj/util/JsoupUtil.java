package com.cqcj.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cqcj.pojo.Commodity;
import com.cqcj.pojo.Result;
import jdk.jshell.execution.Util;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @program: Jsoup-Echarts
 * @description:
 * @author: 王炸！！
 * @create: 2023-05-14 14:04
 **/
public class JsoupUtil {
    public static ArrayList<Commodity> JDgetData(Document document, boolean isdetail ,String brand) {
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
            Double Price = Double.parseDouble(StringPrice);
            //解析商品名字
            Elements nameElements = e.select("div.p-name").select("a").select("em");
            String Name = nameElements.text();
            //解析商品评论数量
            Elements numberElements = e.select("div.p-commit");
            String number = numberElements.text();
            //封装商品信息
            Commodity commodity = new Commodity();
            commodity.setBrand(brand);
            commodity.setName(Name);
            commodity.setPrice(Price);
            commodity.setUrl(Url);
            //详情页超链接
            String href = e.select("div.p-name").select("a").attr("href");
            //解析商品内部Code
            String substring = href.substring(14);
            int end = substring.indexOf(".");
            String Code = substring.substring(0, end);
            String detailUrl = "https:" + href;
            //封装商品Code
            commodity.setCode(Code);
            //获取评价
            //京东评价数量是异步加载
            //请求接口是https://api.m.jd.com/?appid=item-v3&functionId=pc_club_productCommentSummaries&referenceIds={商品Code}
            String APIurl = "https://api.m.jd.com/?appid=item-v3&functionId=pc_club_productCommentSummaries&referenceIds="+Code;

            RestTemplate restTemplate = new RestTemplate();
            String forObject = restTemplate.getForObject(APIurl, String.class);
            JSONObject jo = JSON.parseObject(forObject);
            JSONArray commentsArray = jo.getJSONArray("CommentsCount");
//            System.out.println(commentsArray);
            JSONObject jsonObject = (JSONObject)commentsArray.get(0);
//            System.out.println(o);
            //全部评价
            String CommentCountStr = jsonObject.getString("CommentCountStr");
            if (CommentCountStr.contains("万")){
                int wan = CommentCountStr.indexOf("万");
                CommentCountStr = Double.parseDouble(CommentCountStr.substring(0, wan)) * 10000 +"+";
            }
            commodity.setAllEvaluation(CommentCountStr);
            //好评
            String GoodCountStr = jsonObject.getString("GoodCountStr");
            if (GoodCountStr.contains("万")){
                int wan = GoodCountStr.indexOf("万");
                GoodCountStr = Double.parseDouble(GoodCountStr.substring(0, wan)) * 10000 +"+";
            }
            commodity.setGoodEvaluation(GoodCountStr);
            //中评
            String GeneralCountStr = jsonObject.getString("GeneralCountStr");
            if (GeneralCountStr.contains("万")){
                int wan = GeneralCountStr.indexOf("万");
                GeneralCountStr = Double.parseDouble(GeneralCountStr.substring(0, wan)) * 10000 +"+";
            }
            commodity.setGeneralEvaluation(GeneralCountStr);
            //差评
            String PoorCountStr = jsonObject.getString("PoorCountStr");
            if (PoorCountStr.contains("万")){
                int wan = PoorCountStr.indexOf("万");
                PoorCountStr = Double.parseDouble(PoorCountStr.substring(0, wan)) * 10000 +"+";
            }
            commodity.setPoorEvaluation(PoorCountStr);
            //小心反爬,是否详细查询
            if (isdetail) {
                JsoupUtil.JDgetDetial(detailUrl, commodity);
            }
            //添加到集合中
            arrCommodity.add(commodity);
        });
        return arrCommodity;
    }

    public static Commodity JDgetDetial(String detailUrl, Commodity commodity) {
//        Document document = null;
//        try {
//            document = Jsoup.connect(detailUrl).get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Document document = HtmlUnitUtil.getJdHtml(detailUrl);
        Elements element = document.body().getElementsByClass("p-parameter");
        Elements select = element.select("ul.p-parameter-list");
        Elements eq1 = select.eq(1);
        String brand = eq1.select("li").attr("title");
        commodity.setBrand(brand);

        return commodity;
    }
}
