package com.biao.http;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClientManager {

    private static HttpClientManager clientManager = new HttpClientManager();

    private PoolingHttpClientConnectionManager connectionManager;

    private HttpClientManager() {
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", buildSSLConnectionSocketFactory()).build();
        connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(100);
    }

    public static HttpClientManager instance() {
        return clientManager;
    }

    private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
        try {
            return new SSLConnectionSocketFactory(createIgnoreVerifySSL()); // 优先绕过安全证书
        } catch (KeyManagementException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }

    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        SSLContext sc = SSLContext.getInstance("SSLv3");
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    public CloseableHttpClient generateClient() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(connectionManager);
        // 解决post/redirect/post 302跳转问题
        httpClientBuilder.setRedirectStrategy(new LaxRedirectStrategy() {
            @Override
            public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                URI uri = getLocationURI(request, response, context);
                String method = request.getRequestLine().getMethod();
                if ("post".equalsIgnoreCase(method)) {
                    try {
                        HttpRequestWrapper httpRequestWrapper = (HttpRequestWrapper) request;
                        httpRequestWrapper.setURI(uri);
                        httpRequestWrapper.removeHeaders("Content-Length");
                        return httpRequestWrapper;
                    } catch (Exception e) {
                    }
                    return new HttpPost(uri);
                } else {
                    return new HttpGet(uri);
                }
            }
        });

        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(5000).setSoKeepAlive(true).setTcpNoDelay(true)
                .build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig);
        connectionManager.setDefaultSocketConfig(socketConfig);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(0, true));
        return httpClientBuilder.build();
    }
}
