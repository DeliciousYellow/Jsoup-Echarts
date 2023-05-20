package com.cqcj.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @program: Jsoup-Echarts
 * @description:
 * @author: 王炸！！
 * @create: 2023-05-12 21:27
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_chart")//不同名映射
public class Commodity {
    @TableId(type = IdType.AUTO)//自增主键字段自动赋值给对应的主键属性
    private Integer id;

    private String code;
    private String name;
    private String brand;
    private Double price;
    private String url;

    private String allEvaluation;
    private String goodEvaluation;
    private String generalEvaluation;
    private String poorEvaluation;
}
