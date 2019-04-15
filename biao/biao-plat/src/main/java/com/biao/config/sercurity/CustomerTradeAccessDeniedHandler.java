package com.biao.config.sercurity;

import com.biao.constant.Constants;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface CustomerTradeAccessDeniedHandler extends ServerAccessDeniedHandler {

    Mono<Void> handle(ServerWebExchange exchange, CustomerAuthException denied);

    /**
     * Determines if access is granted for a specific authentication and object.
     *
     * @param authentication the Authentication to check
     * @param object         the object to check
     * @return an decision or empty Mono if no decision could be made.
     */
    Mono<AuthorizationDecision> check(Mono<Authentication> authentication, ServerWebExchange object);

    /**
     * Determines if access should be granted for a specific authentication and object
     *
     * @param authentication the Authentication to check
     * @param object         the object to check
     * @return an empty Mono if authorization is granted or a Mono error if access is
     * denied
     */
    default Mono<Void> verify(Mono<Authentication> authentication, ServerWebExchange object) {
        return check(authentication, object)
                .filter(d -> d.isGranted())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CustomerAuthTradeException(Constants.USER_TRADE_GOOGLE_CODE))))
                .flatMap(d -> Mono.empty());
    }
}
