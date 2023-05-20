package com.cqcj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqcj.pojo.Commodity;

import java.util.List;
import java.util.Map;

/**
 * @program: Jsoup-Echarts
 * @description:
 * @author: 王炸！！
 * @create: 2023-05-12 21:40
 **/
public interface CommodityService extends IService<Commodity> {
    Integer saveByList(List<Commodity> list);
    List<Map<String,String>> GetNameList();
}
