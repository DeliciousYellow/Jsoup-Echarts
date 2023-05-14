package com.cqcj;

import com.cqcj.pojo.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class JsoupEchartsApplicationTests {

    @Test
    void GetData() {
        String A = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&wq=%E6%89%8B%E6%9C%BA&pvid=8858151673f941e9b1a4d2c7214b2b52";
        Document document = null;
        try {
            document = Jsoup.connect(A).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements ul = document.select("ul");
        Elements lis = ul.select("li");

        lis.forEach(i -> {
            Pattern pattern = Pattern.compile("(\\d+(\\.\\d{1,2})?)");
            String text = i.text();
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                String price = matcher.group(1);
                System.out.println(price);
            }
        });
    }

}
