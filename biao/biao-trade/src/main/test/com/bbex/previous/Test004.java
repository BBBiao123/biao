package com.biao.previous;

import java.util.concurrent.locks.LockSupport;

public class Test004 {


    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    LockSupport.parkNanos(1000000000);
                    System.out.println("好吧．．．．．．．．．．．．．");
                }
            }
        }).start();
        Thread.sleep(Integer.MAX_VALUE);
    }

}
