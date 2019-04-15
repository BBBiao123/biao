package com.biao.previous.thread;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 线程路由选择器
 */
public final class ConsistentHashSelector {

    private final TreeMap<Long, SingletonExecutor> virtualInvokers;

    private final int replicaNumber = 160;

    public ConsistentHashSelector(List<SingletonExecutor> selects) {
        this.virtualInvokers = new TreeMap<>();
        for (SingletonExecutor executor : selects) {
            for (int i = 0; i < replicaNumber / 4; i++) {
                byte[] digest = md5(executor.getName() + i);
                for (int h = 0; h < 4; h++) {
                    long m = hash(digest, h);
                    virtualInvokers.put(m, executor);
                }
            }
        }
    }

    public SingletonExecutor select(String key) {
        byte[] digest = md5(key);
        return selectForKey(hash(digest, 0));
    }


    private SingletonExecutor selectForKey(long hash) {
        SingletonExecutor invoker;
        Long key = hash;
        if (!virtualInvokers.containsKey(key)) {
            SortedMap<Long, SingletonExecutor> tailMap = virtualInvokers.tailMap(key);
            if (tailMap.isEmpty()) {
                key = virtualInvokers.firstKey();
            } else {
                key = tailMap.firstKey();
            }
        }
        invoker = virtualInvokers.get(key);
        return invoker;
    }

    /**
     * Ketama 的一个hash算法;
     *
     * @param digest digest;
     * @param number 数值;
     * @return hash值;
     */
    private long hash(byte[] digest, int number) {
        return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                | (digest[number * 4] & 0xFF))
                & 0xFFFFFFFFL;
    }

    private byte[] md5(String value) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        md5.reset();
        byte[] bytes;
        try {
            bytes = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        md5.update(bytes);
        return md5.digest();
    }

}