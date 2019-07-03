package com.biao.util.http;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP网络请求
 *
 * ""王存见
 */
public class HttpRequest {

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     * @throws IOException
     */
    public static String sendGet(String url, String param) throws IOException {
        String result = "";
        BufferedReader in = null;

        String urlNameString = url + "?" + param;
        URL realUrl = new URL(urlNameString);
        // 打开和URL之间的连接
        URLConnection connection = realUrl.openConnection();
        // 设置通用的请求属性
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        //Map<String, List<String>> map = connection.getHeaderFields();
        // 遍历所有的响应头字段
		/*for (String key : map.keySet()) {
			System.out.println(key + "--->" + map.get(key));
		}*/
        // 定义 BufferedReader输入流来读取URL的响应
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        // 输入流
        if (in != null) {
            in.close();
        }
        return result;
    }

    public static byte[] sendGet(String url) throws IOException {
        InputStream in = null;
        String urlNameString = url;
        URL realUrl = new URL(urlNameString);
        // 打开和URL之间的连接
        URLConnection connection = realUrl.openConnection();
        // 设置通用的请求属性
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        //Map<String, List<String>> map = connection.getHeaderFields();
        // 遍历所有的响应头字段
		/*for (String key : map.keySet()) {
			System.out.println(key + "--->" + map.get(key));
		}*/
        // 定义 BufferedReader输入流来读取URL的响应
        in = connection.getInputStream();
        byte[] datas = ByteToInputStream.input2byte(in);
        // 输入流
        if (in != null) {
            in.close();
        }
        return datas;
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param Map方式请求
     * @return 所代表远程资源的响应结果
     * @throws IOException
     */
    public static String sendGet(String url, Map<String, String> param) throws IOException {
        String paramStr = "";
        if (null != param) {
            for (String key : param.keySet()) {
                if (!StringUtils.isEmpty(paramStr)) {
                    paramStr += "&";
                }
                paramStr += key + "=" + param.get(key);
            }
        }
        return sendGet(url, paramStr);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     * @throws IOException
     */
    public static String sendPost(String url, String param) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        out.print(param);
        // flush输出流的缓冲
        out.flush();
        // 定义BufferedReader输入流来读取URL的响应
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        if (out != null) {
            out.close();
        }
        if (in != null) {
            in.close();
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param Map方式请求
     * @return 所代表远程资源的响应结果
     * @throws IOException
     */
    public static String sendPost(String url, Map<String, String> param) throws IOException {
        String paramStr = "";
        if (null != param) {
            for (String key : param.keySet()) {
                if (!StringUtils.isEmpty(paramStr)) {
                    paramStr += "&";
                }
                paramStr += key + "=" + param.get(key);
            }
        }
        return sendPost(url, paramStr);
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> param =  new HashMap<>();
        param.put("stoken","3bb592c5036c41d9885e73d0b684bd92");
        String result =  HttpRequest.sendGet("http://www.coceu.com/biao/balance/volume/change",param);
        System.out.println(result);

        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        OkHttpClient client = mBuilder
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url("http://www.coceu.com/biao/user/invotes?currentPage=0&showCount=10")
                .addHeader("stoken","3bb592c5036c41d9885e73d0b684bd92")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().toString());
    }
}