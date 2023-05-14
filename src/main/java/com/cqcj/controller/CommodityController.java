package com.cqcj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqcj.pojo.Commodity;
import com.cqcj.pojo.Result;
import com.cqcj.service.CommodityService;
import com.cqcj.util.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @program: Jsoup-Echarts
 * @description:
 * @author: 王炸！！
 * @create: 2023-05-12 21:34
 **/
@RestController
@CrossOrigin
public class CommodityController {
    @Resource
    private CommodityService commodityService;

    @PostMapping("/PostDataByJD")
    public Result getCommodityInformation(@RequestBody String JsonUrl) {
        JSONObject jsonObject = JSON.parseObject(JsonUrl);
        String url = jsonObject.getString("url");

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail().setMessage("网页爬取失败");
        }
        ArrayList<Commodity> arrCommodity = JsoupUtil.JDgetData(document);
//        arrCommodity.forEach(System.out::println);
        Integer count = commodityService.saveByList(arrCommodity);

        return Result.ok(arrCommodity).setMessage("成功爬取并保存了"+count+"条数据");
    }
}
