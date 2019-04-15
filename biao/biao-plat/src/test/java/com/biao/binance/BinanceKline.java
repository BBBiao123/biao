package com.biao.binance;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *  ""(""611 @ qq.com)
 */
public class BinanceKline {

    private static final Gson GOSN = new Gson();

    @Test
    public void acquiredKline() {


        String url = "https://www.binance.com/api/v1/klines?symbol=BTCUSDT&interval=1w";
        try {

            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1080));//设置socks代理服务器ip端口
            java.net.Authenticator.setDefault(new java.net.Authenticator()//由于okhttp好像没有提供socks设置Authenticator用户名密码接口，因此设置一个全局的Authenticator
            {
                private PasswordAuthentication authentication = new PasswordAuthentication("username", "password".toCharArray());

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return authentication;
                }
            });

            OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
            OkHttpClient client = mBuilder
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .proxy(proxy)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();

            assert response.body() != null;
            final String result = response.body().string();
            System.out.println(result);
            Type type = new TypeToken<List<List<Object>>>() {
            }.getType();
            List<List<Object>> r = GOSN.fromJson(result, type);
            for (List<Object> objects : r) {
                final String s = objects.get(0).toString();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class Bean {
        List<Object> strings;

        public List<Object> getStrings() {
            return strings;
        }

        public void setStrings(List<Object> strings) {
            this.strings = strings;
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }
}
