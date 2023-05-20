package com.cqcj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqcj.pojo.Commodity;
import com.cqcj.pojo.Result;
import com.cqcj.service.CommodityService;
import com.cqcj.util.HtmlUnitUtil;
import com.cqcj.util.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @program: Jsoup-Echarts
 * @description:
 * @author: 王炸！！
 * @create: 2023-05-12 21:34
 **/
@RestController
@CrossOrigin
@SessionAttributes("arrCommodity")
public class CommodityController {
    @Resource
    private CommodityService commodityService;

    private ArrayList<Commodity> arrCommodity;

    @GetMapping("/GetJdByUrl/{encodeURI}/{isdetial}")
    public Result GetJdByUrl(@PathVariable String encodeURI, @PathVariable boolean isdetial , HttpServletRequest request) {
        String Url;
        try {
            Url = java.net.URLDecoder.decode(encodeURI, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return Result.fail().setMessage("编码解析错误");
        }
//        JSONObject jsonObject = JSON.parseObject(Url);
//        String url = jsonObject.getString("url");
        Document document;
        try {
            document = Jsoup.connect(Url).get();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail().setMessage("网页爬取失败");
        }
        ArrayList<Commodity> arrCommodity = JsoupUtil.JDgetData(document,isdetial,null);
//        arrCommodity.forEach(System.out::println);
//        Integer count = commodityService.saveByList(arrCommodity);
        HttpSession session = request.getSession();
        session.setAttribute("arrCommodity", arrCommodity);
        this.arrCommodity = arrCommodity;

        return Result.ok(arrCommodity).setMessage("成功爬取了" + arrCommodity.size() + "条数据");
    }

    @GetMapping("/GetJdByName/{name}/{number}/{isdetial}")
    public Result GetJdByName(@PathVariable String name, @PathVariable Integer number, @PathVariable boolean isdetial, HttpServletRequest request) {
        String url = "https://search.jd.com/Search?keyword=" + name;
        ArrayList<Commodity> arrCommodity = new ArrayList<>();
        for (Integer i = 1; i < number / 15; i += 2) {
            Document document;
            try {
                document = Jsoup.connect(url + "&page=" + i.toString()).get();
            } catch (Exception e) {
                e.printStackTrace();
                return Result.fail().setMessage("网页爬取失败");
            }
            arrCommodity.addAll(JsoupUtil.JDgetData(document,isdetial,name));
        }
        HttpSession session = request.getSession();
        session.setAttribute("arrCommodity", arrCommodity);
        this.arrCommodity = arrCommodity;
        return Result.ok(arrCommodity).setMessage("成功爬取了" + arrCommodity.size() + "条数据");
    }

    @PostMapping("/DoSave")
//    @SessionAttribute("arrCommodity") ArrayList<Commodity> arrCommodity
    public Result DoSave(HttpSession session) {
//        ArrayList<Commodity> arrCommodity = (ArrayList<Commodity>)session.getAttribute("arrCommodity");
//        Object arrCommodity = session.getAttribute("arrCommodity");
        ArrayList<Commodity> arrCommodity = this.arrCommodity;
        if (arrCommodity == null) {
            return Result.fail().setMessage("需要先爬取再存储");
        } else {
            Integer count = commodityService.saveByList(arrCommodity);
            return Result.ok(arrCommodity).setMessage("成功保存了" + count + "条数据");
        }
    }

    @GetMapping("/GetCommodityName")
    public String GetCommodityName() {
//        LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();
        List<Map<String,String>> arrName = commodityService.GetNameList();
        return JSON.toJSONString(arrName);
    }

    @GetMapping("/GetCount")
    public String GetCount() {
        int count = commodityService.count();
        Map<String, Integer> map = new HashMap<>();
        map.put("value",count);
        return "["+JSON.toJSONString(map)+"]";
    }

    private String[] ArrCode = {"100047451618","100019459625","100031192618","100037311057","100037437645"};

    @GetMapping("/GetAllEvaluation")
    public String GetAllEvaluation() {
        List<Map<Object, Object>> list = new ArrayList<>();
        for (int i = 0; i < ArrCode.length; i++) {
            Map<Object, Object> map = new HashMap<>();
            LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Commodity::getCode,ArrCode[i]);
            Commodity one = commodityService.getOne(wrapper);
            String allEvaluation = one.getAllEvaluation();
            if (allEvaluation.contains("+")){
                allEvaluation = allEvaluation.substring(0, allEvaluation.length()-1);
            }
            map.put("value",Double.parseDouble(allEvaluation));
            map.put("name",one.getName());
            list.add(map);
        }
        return JSON.toJSONString(list);
    }

    @GetMapping("/GetEvaluationContrast")
    public String GetEvaluationContrast() {
        List<Map<Object, Object>> list = new ArrayList<>();
        for (int i = 0; i < ArrCode.length; i++) {
            Map<Object, Object> goodmap = new HashMap<>();
            Map<Object, Object> generalmap = new HashMap<>();
            Map<Object, Object> poormap = new HashMap<>();
            LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Commodity::getCode,ArrCode[i]);
            Commodity one = commodityService.getOne(wrapper);
            String goodEvaluation = one.getGoodEvaluation();
            String generalEvaluation = one.getGeneralEvaluation();
            String poorEvaluation = one.getPoorEvaluation();
            if (goodEvaluation.contains("+")){
                goodEvaluation = goodEvaluation.substring(0, goodEvaluation.length()-1);
            }
            if (generalEvaluation.contains("+")){
                generalEvaluation = generalEvaluation.substring(0, generalEvaluation.length()-1);
            }
            if (poorEvaluation.contains("+")){
                poorEvaluation = poorEvaluation.substring(0, poorEvaluation.length()-1);
            }
            goodmap.put("time",one.getName());
            goodmap.put("value",Double.parseDouble(goodEvaluation));
            goodmap.put("name","好评");
            list.add(goodmap);
            generalmap.put("time",one.getName());
            generalmap.put("value",Double.parseDouble(generalEvaluation));
            generalmap.put("name","中评");
            list.add(generalmap);
            poormap.put("time",one.getName());
            poormap.put("value",Double.parseDouble(poorEvaluation));
            poormap.put("name","差评");
            list.add(poormap);
        }
        return JSON.toJSONString(list);
    }

    private String[] ArrBrand = {"OPPO","VIVO"};

    @GetMapping("/GetCommodityByBrand")
    public String GetCommodityByBrand() {
        List<Map<Object, Object>> list = new ArrayList<>();
        for (int i = 0; i < ArrBrand.length; i++) {
            double ZongXiaoLiang = 0.0;
            double HaoPing = 0.0;
            double ChaPing = 0.0;
            Double Hight = -1.0;
            Double Low = 999999999.0;
            Double Allprice = 0.0;
            LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Commodity::getBrand,ArrBrand[i]);
            List<Commodity> commodityList = commodityService.list(wrapper);
            for (int j = 0; j < commodityList.size(); j++) {
                Commodity c = commodityList.get(j);
                //计算品牌总销量A
                String allEvaluation = c.getAllEvaluation();
                if (allEvaluation.contains("+")){
                    allEvaluation = allEvaluation.substring(0, allEvaluation.length()-1);
                }
                ZongXiaoLiang += Double.parseDouble(allEvaluation);
                //计算品牌好评总数量
                String goodEvaluation = c.getGoodEvaluation();
                if (goodEvaluation.contains("+")){
                    goodEvaluation = goodEvaluation.substring(0, goodEvaluation.length()-1);
                }
                HaoPing += Double.parseDouble(goodEvaluation);
                //计算品牌差评总数量
                String poorEvaluation = c.getPoorEvaluation();
                if (poorEvaluation.contains("+")){
                    poorEvaluation = poorEvaluation.substring(0, poorEvaluation.length()-1);
                }
                ChaPing += Double.parseDouble(poorEvaluation);
                double price = c.getPrice();
                //计算品牌总价格
                Allprice+=price;
                //计算品牌最高价格
                Hight = price > Hight ? price : Hight;
                //计算品牌最低价格
                Low = price < Low ? price : Low;
            }
            Double average = Allprice/commodityList.size();
            int i1 = String.valueOf(average).indexOf(".");
            average = Double.parseDouble(String.valueOf(average).substring(0, i1 + 3));

            Map<Object, Object> map1 = new HashMap<>();
            map1.put("time","品牌总销量单位×10^3");
            map1.put("value",ZongXiaoLiang/1000);
            map1.put("name",ArrBrand[i]);
            list.add(map1);
            Map<Object, Object> map2 = new HashMap<>();
            map2.put("time","品牌好评总数量×10^3");
            map2.put("value",HaoPing/1000);
            map2.put("name",ArrBrand[i]);
            list.add(map2);
            Map<Object, Object> map3 = new HashMap<>();
            map3.put("time","品牌差评总数量");
            map3.put("value",ChaPing);
            map3.put("name",ArrBrand[i]);
            list.add(map3);
            Map<Object, Object> map4 = new HashMap<>();
            map4.put("time","品牌平均价格");
            map4.put("value",average);
            map4.put("name",ArrBrand[i]);
            list.add(map4);
            Map<Object, Object> map5 = new HashMap<>();
            map5.put("time","品牌最高价格");
            map5.put("value",Hight);
            map5.put("name",ArrBrand[i]);
            list.add(map5);
            Map<Object, Object> map6 = new HashMap<>();
            map6.put("time","品牌最低价格");
            map6.put("value",Low);
            map6.put("name",ArrBrand[i]);
            list.add(map6);
        }
        return JSON.toJSONString(list);
    }

}
