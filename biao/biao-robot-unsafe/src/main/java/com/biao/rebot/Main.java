package com.biao.rebot;

import com.biao.rebot.service.async.BinanceAsyncData;

/**
 * 启动机器人；
 *
 *
 */
public class Main {

    public static void main(String[] args) {
        new AsyncRobotService<BinanceAsyncData>().start();
    }
}
