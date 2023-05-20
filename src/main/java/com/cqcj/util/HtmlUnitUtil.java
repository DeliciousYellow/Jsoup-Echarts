package com.cqcj.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Iterator;

/**
 * @program: Jsoup-Echarts
 * @description:
 * @author: 王炸！！
 * @create: 2023-05-20 20:33
 **/
public class HtmlUnitUtil {
    public static Document getJdHtml(String Url) {
        try {

            WebClient wc = new WebClient(BrowserVersion.CHROME);

            // 启用JS解释器，默认为true
            wc.getOptions().setJavaScriptEnabled(true);

            // 禁用css支持
            wc.getOptions().setCssEnabled(false);

            // js运行错误时，是否抛出异常
            wc.getOptions().setThrowExceptionOnScriptError(false);

            // 状态码错误时，是否抛出异常
            wc.getOptions().setThrowExceptionOnFailingStatusCode(false);

            // 设置连接超时时间 ，这里是5S。如果为0，则无限期等待
            wc.getOptions().setTimeout(5000);

            // 是否允许使用ActiveX
            wc.getOptions().setActiveXNative(false);

            // 等待js时间
            wc.waitForBackgroundJavaScript(1 * 1000);

            // 设置Ajax异步处理控制器即启用Ajax支持
            wc.setAjaxController(new NicelyResynchronizingAjaxController());

            // 不跟踪抓取
            wc.getOptions().setDoNotTrackEnabled(false);

            // 访问页面
            HtmlPage page = wc.getPage(Url);
//            //模拟点击界面
//            Iterable<DomElement> detail = page.getElementById("detail").getFirstElementChild().getFirstElementChild().getChildElements();
//            Iterator<DomElement> iterator = detail.iterator();
//            iterator.next();
//            iterator.next();
//            iterator.next();
//            iterator.next();
//            DomElement li = iterator.next();
//            li.click();
//            // 等待页面加载完成
//            wc.waitForBackgroundJavaScript(5000);
//            // 获取点击后的页面内容
            String pageContent = page.asXml();
            Document document = Jsoup.parse(pageContent);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
