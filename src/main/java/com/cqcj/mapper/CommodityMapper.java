package com.cqcj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqcj.pojo.Commodity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: Jsoup-Echarts
 * @description:
 * @author: 王炸！！
 * @create: 2023-05-12 21:33
 **/
public interface CommodityMapper extends BaseMapper<Commodity>{
    @Select("SELECT `name` FROM t_chart")
    List<Commodity> SelectNameList();
}