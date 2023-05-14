package com.cqcj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqcj.mapper.CommodityMapper;
import com.cqcj.pojo.Commodity;
import com.cqcj.service.CommodityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: Jsoup-Echarts
 * @description:
 * @author: 王炸！！
 * @create: 2023-05-12 21:40
 **/
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    @Resource
    private CommodityMapper commodityMapper;
    @Override
    public Integer saveByList(List<Commodity> list) {
        AtomicInteger count = new AtomicInteger();
        list.forEach(commodity -> {
            int insert = commodityMapper.insert(commodity);
            count.addAndGet(insert);
        });
        return count.get();
    }
}
