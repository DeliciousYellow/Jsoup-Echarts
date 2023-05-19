package com.cqcj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqcj.pojo.Commodity;
import com.cqcj.pojo.Result;
import com.cqcj.service.CommodityService;
import com.cqcj.util.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/GetJdByUrl/{encodeURI}")
    public Result GetJdByUrl(@PathVariable String encodeURI,HttpServletRequest request) {
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
        ArrayList<Commodity> arrCommodity = JsoupUtil.JDgetData(document);
//        arrCommodity.forEach(System.out::println);
//        Integer count = commodityService.saveByList(arrCommodity);
        HttpSession session = request.getSession();
        session.setAttribute("arrCommodity",arrCommodity);
        this.arrCommodity = arrCommodity;

        return Result.ok(arrCommodity).setMessage("成功爬取了" + arrCommodity.size() +"条数据");
    }

    @GetMapping("/GetJdByName/{name}/{number}")
    public Result GetJdByName(@PathVariable String name,@PathVariable Integer number ,HttpServletRequest request) {
        String url = "https://search.jd.com/Search?keyword="+name;
        ArrayList<Commodity> arrCommodity = new ArrayList<>();
        for (Integer i = 1; i < number/15; i+=2) {
            Document document;
            try {
                document = Jsoup.connect(url+"&page="+i.toString()).get();
            } catch (Exception e) {
                e.printStackTrace();
                return Result.fail().setMessage("网页爬取失败");
            }
            arrCommodity.addAll(JsoupUtil.JDgetData(document));
        }

        HttpSession session = request.getSession();
        session.setAttribute("arrCommodity",arrCommodity);
        this.arrCommodity = arrCommodity;

        return Result.ok(arrCommodity).setMessage("成功爬取了" + arrCommodity.size() +"条数据");
    }

    @PostMapping("/DoSave")
//    @SessionAttribute("arrCommodity") ArrayList<Commodity> arrCommodity
    public Result DoSave(HttpSession session) {
//        ArrayList<Commodity> arrCommodity = (ArrayList<Commodity>)session.getAttribute("arrCommodity");
//        Object arrCommodity = session.getAttribute("arrCommodity");
        ArrayList<Commodity> arrCommodity = this.arrCommodity;
        if(arrCommodity==null){
            return Result.fail().setMessage("需要先爬取再存储");
        }else {
//            Integer count = commodityService.saveByList(arrCommodity);
            return Result.ok(arrCommodity).setMessage("成功保存了" + 1 +"条数据");
        }
    }

}
