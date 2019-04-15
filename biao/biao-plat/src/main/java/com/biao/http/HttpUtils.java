package com.biao.http;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class HttpUtils {

    private static final Pattern patternForCharset = Pattern.compile("charset\\s*=\\s*['\"]*([^\\s;'\"]*)");

    public static String getGetRequestData(String urlRequest) {
        return getRequestData(urlRequest, "GET", null, null);
    }

    public static String getGetRequestData(String urlRequest, Map<String, String> nameValuePairMap) {
        return getRequestData(urlRequest, "GET", null, nameValuePairMap);
    }

    public static String getPostRequestData(String urlRequest) {
        return getRequestData(urlRequest, "POST", null, null);
    }

    public static String getPostRequestData(String urlRequest, Map<String, String> nameValuePairMap) {
        return getRequestData(urlRequest, "POST", null, nameValuePairMap);
    }

    public static String getRequestData(String urlRequest, String method, Map<String, String> nameValuePairMap) {
        return getRequestData(urlRequest, method, null, nameValuePairMap);
    }

    public static String getRequestData(String urlRequest, String method, Map<String, String> headers, Map<String, String> nameValuePairMap) {
        CloseableHttpResponse httpResponse = null;
        int statusCode = 0;
        try {
            HttpUriRequest httpUriRequest = getHttpUriRequest(urlRequest, method, headers, nameValuePairMap);
            httpResponse = HttpClientManager.instance().generateClient().execute(httpUriRequest);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                return handleResponse(Charset.defaultCharset(), httpResponse);
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (httpResponse != null) {
                    // ensure the connection is released back to pool
                    EntityUtils.consume(httpResponse.getEntity());
                }
            } catch (IOException e) {
            }
        }
    }

    private static HttpUriRequest getHttpUriRequest(String urlRequest, String method, Map<String, String> headers,
                                                    Map<String, String> nameValuePairMap) {
        RequestBuilder requestBuilder = selectRequestMethod(method, nameValuePairMap).setUri(urlRequest);
        if (headers != null) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom().setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000).setConnectTimeout(30000);
        requestBuilder.setConfig(requestConfigBuilder.build());
        return requestBuilder.build();
    }

    private static RequestBuilder selectRequestMethod(String method, Map<String, String> nameValuePairMap) {
        if (method == null || method.equalsIgnoreCase("GET")) {
            // default get
            return RequestBuilder.get();
        } else if (method.equalsIgnoreCase("POST")) {
            RequestBuilder requestBuilder = RequestBuilder.post();
            if (nameValuePairMap != null) {
                NameValuePair[] nameValuePairs = (NameValuePair[]) nameValuePairMap.entrySet().stream().map(entry -> {
                    NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                    return nameValuePair;
                }).collect(Collectors.toList()).toArray(new NameValuePair[]{});
                if (nameValuePairs != null && nameValuePairs.length > 0) {
                    requestBuilder.addParameters(nameValuePairs);
                }
            }
            return requestBuilder;
        } else if (method.equalsIgnoreCase("HEAD")) {
            return RequestBuilder.head();
        } else if (method.equalsIgnoreCase("PUT")) {
            return RequestBuilder.put();
        } else if (method.equalsIgnoreCase("DELETE")) {
            return RequestBuilder.delete();
        } else if (method.equalsIgnoreCase("TRACE")) {
            return RequestBuilder.trace();
        }
        throw new IllegalArgumentException("Illegal HTTP Method " + method);
    }

    private static String handleResponse(Charset defaultCharset, CloseableHttpResponse httpResponse) throws IOException {
        return getContent(defaultCharset.toString(), httpResponse);
    }

    private static String getContent(String charset, HttpResponse httpResponse) throws IOException {
        if (charset == null) {
            byte[] contentBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            String htmlCharset = getHtmlCharset(httpResponse, contentBytes);
            if (htmlCharset != null) {
                return new String(contentBytes, htmlCharset);
            } else {
                return new String(contentBytes);
            }
        } else {
            return IOUtils.toString(httpResponse.getEntity().getContent(), charset);
        }
    }

    private static String getHtmlCharset(HttpResponse httpResponse, byte[] contentBytes) throws IOException {
        String charset;
        // charset
        // 1、encoding in http header Content-Type
        String value = httpResponse.getEntity().getContentType().getValue();
        charset = getCharset(value);
        if (StringUtils.isNotBlank(charset)) {
            return charset;
        }
        // use default charset to decode first time
        Charset defaultCharset = Charset.defaultCharset();
        String content = new String(contentBytes, defaultCharset.name());
        // 2、charset in meta
        if (StringUtils.isNotEmpty(content)) {
            Document document = Jsoup.parse(content);
            Elements links = document.select("meta");
            for (Element link : links) {
                // 2.1、html4.01 <meta http-equiv="Content-Type" content="text/html;
                // charset=UTF-8" />
                String metaContent = link.attr("content");
                String metaCharset = link.attr("charset");
                if (metaContent.indexOf("charset") != -1) {
                    metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
                    charset = metaContent.split("=")[1];
                    break;
                }
                // 2.2、html5 <meta charset="UTF-8" />
                else if (StringUtils.isNotEmpty(metaCharset)) {
                    charset = metaCharset;
                    break;
                }
            }
        }
        // 3、todo use tools as cpdetector for content decode
        return charset;
    }

    public static String getCharset(String contentType) {
        Matcher matcher = patternForCharset.matcher(contentType);
        if (matcher.find()) {
            String charset = matcher.group(1);
            if (Charset.isSupported(charset)) {
                return charset;
            }
        }
        return null;
    }
}
