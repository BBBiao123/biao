package com.biao.rebot.common;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个并发安全的HashSet实现，内部通过ConcurrentHashMap，进行封装实现。
 * KEY：做为HashSet的Value
 * VALUE:为一个固定对象PRESENT
 *
 * @param <E> the type parameter
 *  "" sixh.
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E>, java.io.Serializable {

    /**
     * The constant PRESENT.
     */
    private static final Object PRESENT = new Object();
    /**
     * The Map.
     */
    private final ConcurrentHashMap<E, Object> map;

    /**
     * Instantiates a new Concurrent hash set.
     */
    public ConcurrentHashSet() {
        this.map = new ConcurrentHashMap<>();
    }

    /**
     * Instantiates a new Concurrent hash set.
     *
     * @param initialCapacity the initial capacity
     */
    public ConcurrentHashSet(int initialCapacity) {
        map = new ConcurrentHashMap<>(initialCapacity);
    }


    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }
}
