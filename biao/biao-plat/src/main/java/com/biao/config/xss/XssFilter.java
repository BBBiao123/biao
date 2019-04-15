package com.biao.config.xss;

import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class XssFilter implements WebFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(XssFilter.class);

    private ServerWebExchangeMatcher requiresAuthenticationMatcher = ServerWebExchangeMatchers.anyExchange();

    private static final String regularExpression = "^.*[I|i][F|f][R|r][A|a][M|m][E|e].*|.*[S|s][C|c][R|r][I|i][P|p][T|t].*|.*[a|A]('%2b')?[l|L]('%2b')?[e|E]('%2b')?[r|R]('%2b')?[t|T]\\s*\\(.*\\).*|.*[W|w][I|i][N|n][D|d][O|o][W|w]\\.[L|l][O|o][C|c][A|a][T|t][I|i][O|o][N|n]\\s*=.*|.*[S|s][T|t][Y|y][L|l][E|e]\\s*=.*[X|x]:[E|e][X|x].*[P|p][R|r][E|e][S|s]{1,2}[I|i][O|o][N|n]\\s*\\(.*\\).*|.*[D|d][O|o][C|c][U|u][M|m][E|e][N|n][T|t]\\.[C|c][O|o]{2}[K|k][I|i][E|e].*|.*[E|e][V|v][A|a][L|l]\\s*\\(.*\\).*|.*[U|u][N|n][E|e][S|s][C|c][A|a][P|p][E|e]\\s*\\(.*\\).*|.*[E|e][X|x][E|e][C|c][S|s][C|c][R|r][I|i][P|p][T|t]\\s*\\(.*\\).*|.*[M|m][S|s][G|g][B|b][O|o][X|x]\\s*\\(.*\\).*|.*[C|c][O|o][N|n][F|f][I|i][R|r][M|m]\\s*\\(.*\\).*|.*[P|p][R|r][O|o][M|m][P|p][T|t]\\s*\\(.*\\).*|.*<[S|s][C|c][R|r][I|i][P|p][T|t].*>.*</[S|s][C|c][R|r][I|i][P|p][T|t]>.*|.*<[S|s][T|t][Y|y][L|l][E|e].*>.*</[S|s][T|t][Y|y][L|l][E|e]>.*|[.&[^\\\"]]*\\\"[.&[^\\\"]]*|[.&[^']]*'[.&[^']]*|[[.&[^a]]|[|a|\\n|\\r\\n|\\r|\\u0085|\\u2028|\\u2029]]*<[S|s][C|c][R|r][I|i][P|p][T|t]>.*</[S|s][C|c][R|r][I|i][P|p][T|t]>[[.&[^a]]|[|a|\\n|\\r\\n|\\r|\\u0085|\\u2028|\\u2029]]*$\n";

    private static Pattern XSS_PATTERN = Pattern.compile(regularExpression);

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        return this.requiresAuthenticationMatcher.matches(serverWebExchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .switchIfEmpty(webFilterChain.filter(serverWebExchange).then(Mono.empty()))
                .flatMap(matchResult -> {
                    //进行验证
                    final ServerHttpRequest request = serverWebExchange.getRequest();
                    final HttpHeaders headers = request.getHeaders();
                    final MultiValueMap<String, String> queryParams = serverWebExchange.getRequest().getQueryParams();
                    if (queryParams.size() > 0) {
                        for (Object o : queryParams.entrySet()) {
                            Map.Entry entry = (Map.Entry) o;
                            String value = String.valueOf(entry.getValue());
                            if (StringUtils.isNoneBlank(value)) {
                                if (XSS_PATTERN.matcher(value).find()) {
                                    LOGGER.error("#疑似XSS注入攻击！参数名称：{}；录入信息:{}", entry.getKey(), value);
                                    return Mono.just(false);
                                }
                            }
                        }
                    }
                    final HttpMethod method = request.getMethod();
                    if (HttpMethod.POST == method) {
                        return serverWebExchange.getFormData().map(m -> {
                            for (Object o : m.entrySet()) {
                                Map.Entry entry = (Map.Entry) o;
                                String value = String.valueOf(entry.getValue());
                                if (StringUtils.isNoneBlank(value)) {
                                    if (XSS_PATTERN.matcher(value).find()) {
                                        LOGGER.error("#疑似XSS注入攻击！参数名称：{}；录入信息:{}", entry.getKey(), value);
                                        return false;
                                    }
                                }
                            }
                            return true;
                        });
                    }
                    return Mono.just(true);
                })
                .flatMap(results -> results ? webFilterChain.filter(serverWebExchange) : write(serverWebExchange));
    }

    private Mono<Void> write(ServerWebExchange serverWebExchange) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        final GlobalMessageResponseVo result = GlobalMessageResponseVo.newErrorInstance("您输入的参数有非法字符，请输入正确的参数！");
        return response.writeWith(Mono.just(response.bufferFactory().wrap(JsonUtils.toJson(result).getBytes())));
    }

    public static boolean matches(String text) {
        if (text == null) {
            return false;
        }
        return XSS_PATTERN.matcher(text).matches();
    }

    public void setRequiresAuthenticationMatcher(ServerWebExchangeMatcher requiresAuthenticationMatcher) {
        this.requiresAuthenticationMatcher = requiresAuthenticationMatcher;
    }
}