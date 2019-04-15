package com.biao;

import com.biao.util.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;

public class Test {
    public static void main(String args[]) throws IOException {

        BigDecimal a = BigDecimal.valueOf(400.0000000000000000);
        if(a.compareTo(BigDecimal.ZERO) ==-1){
            System.out.println("xiao yu 0");
        }
        System.out.println(a.compareTo(BigDecimal.ZERO));
        //String ip = "163.125.208.111";
        //String ip = "123.125.71.38";
        //String str = getip(ip);
        //System.out.println(str);
        String mobile = "18664234972";
        String url = "http://www.ip138.com:8080/search.asp?action=mobile&mobile=%s";
        url = String.format(url, mobile);
        Document doc = Jsoup.connect(url).get();
        Elements els = doc.getElementsByClass("tdc2");
        System.out.println("归属地：" + els.get(1).text());
        System.out.println("类型：" + els.get(2).text());
        System.out.println("区号：" + els.get(3).text());
        System.out.println("邮编：" + els.get(4).text().substring(0, 6));
    }


    public static String getip(String ip) {
        try {
            String httpUrl = "http://ip.taobao.com/service/getIpInfo.php";
            return HttpRequest.sendGet(httpUrl, "ip=" + ip);
        } catch (Exception e) {
            return null;
        }
    }

}