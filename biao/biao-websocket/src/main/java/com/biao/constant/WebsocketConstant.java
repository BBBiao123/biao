package com.biao.constant;

import io.netty.util.AttributeKey;

/**
 * this is websocket constant.
 *
 *  ""(Myth)
 */
public interface WebsocketConstant {

    String WS_PLAT = "/biao/websocket/plat";

    String WS_BUY_SELL = "/biao/websocket/buyAndSell";

    String WS_USER_ORDER = "/biao/websocket/userOrder";

    String WS_USER_VOLUME = "/biao/websocket/userVolume";

    String WS_KLINE = "/biao/websocket/kline";

    String WS_C2C = "/biao/websocket/c2cUser";

    String WS_HOME = "/biao/websocket/home";

    String WS_MESSAGE = "/biao/websocket/message";

    String FLOWING_WATER = "/biao/websocket/flowingWater";

    String WS_RELAY_PRIZE = "/biao/websocket/relay";

    String WS_LUCKY_DRAW = "/biao/websocket/lucky";

    String PING = "ping";

    String PONG = "pong";

    String JOIN = "_";

    int HTTP_SUCCESS_CODE = 200;

    AttributeKey<String> KLINE_KEY = AttributeKey.valueOf("kline");

    AttributeKey<String> FLOWING_WATER_KEY = AttributeKey.valueOf("flowingWater");

    AttributeKey<String> USER_ORDER_KEY = AttributeKey.valueOf("user_order");

    AttributeKey<String> C2C_USER_ORDER_KEY = AttributeKey.valueOf("c2c_user_order");

    AttributeKey<String> MESSAGE_KEY = AttributeKey.valueOf("message");

}
