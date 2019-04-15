package com.biao.router;

import com.biao.service.OmniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 *  ""
 */
@Component
public class UsdtHttpRouter {

    private final OmniService omniService;

    @Autowired
    public UsdtHttpRouter(OmniService omniService) {
        this.omniService = omniService;
    }

    public RouterFunction<ServerResponse> routes() {
        return nest(path(""),
                route(GET("/getAddress"), this::getAddress));
    }

    private Mono<ServerResponse> getAddress(ServerRequest req) {
        String address = omniService.getNewAddress();
        return ok().body(Mono.just(address), String.class);

    }

}
