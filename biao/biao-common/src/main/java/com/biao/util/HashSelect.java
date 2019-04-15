package com.biao.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * HashSelect.
 * <p>
 * hash选择算法.
 * <p>
 * 19-1-2下午3:08
 *
 *  "" sixh
 */
public class HashSelect {

    private final TreeMap<Long, Integer> virtualInvokers;

    private final int replicaNumber = 160;

    /**
     * Instantiates a new Hash select.
     *
     * @param selects the selects
     */
    public HashSelect(List<Integer> selects) {
        this.virtualInvokers = new TreeMap<>();
        for (Integer need : selects) {
            for (int i = 0; i < replicaNumber / 4; i++) {
                byte[] digest = md5(need+ "-" + i);
                for (int h = 0; h < 4; h++) {
                    long m = hash(digest, h);
                    virtualInvokers.put(m, need);
                }
            }
        }
    }

    /**
     * Select integer.
     *
     * @param key the key
     * @return the integer
     */
    public Integer select(String key) {
        byte[] digest = md5(key);
        return selectForKey(hash(digest, 0));
    }


    private Integer selectForKey(long hash) {
        Integer invoker;
        Long key = hash;
        if (!virtualInvokers.containsKey(key)) {
            SortedMap<Long, Integer> tailMap = virtualInvokers.tailMap(key);
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

    /**
     * Create hash select.
     *
     * @param need the need 种子元素.
     * @return the hash select
     */
    public static HashSelect create(Integer need) {
        List<Integer> needs = new ArrayList<>();
        for(int i=1;i<= need;i++) {
            needs.add(i);
        }
        return new HashSelect(needs);
     }
}
