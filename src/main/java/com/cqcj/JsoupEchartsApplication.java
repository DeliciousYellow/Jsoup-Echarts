package com.cqcj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cqcj.mapper")
public class JsoupEchartsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsoupEchartsApplication.class, args);
    }

}
