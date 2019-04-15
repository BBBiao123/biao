package com.biao.rebot.common;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * DuplicateBlockingQueue.
 * <p>
 *     去重的队列处理.
 * <p>
 * 18-12-19下午4:15
 *
 *  "" sixh
 */
@SuppressWarnings("all")
public class DuplicateBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {


    /** The capacity bound, or Integer.MAX_VALUE if none */
    private final int capacity;

    /** Lock held by take, poll, etc */
    private final ReentrantLock takeLock = new ReentrantLock();

    /** Wait queue for waiting takes */
    private final Condition notEmpty = takeLock.newCondition();

    /** Lock held by put, offer, etc */
    private final ReentrantLock putLock = new ReentrantLock();

    /** Wait queue for waiting puts */
    private final Condition notFull = putLock.newCondition();

    /** Current number of elements */
    private final AtomicInteger count = new AtomicInteger();

    /**
     * 用于hashSet
     */
    private ConcurrentHashSet<E> element = new ConcurrentHashSet<>();

    public DuplicateBlockingQueue() {
        capacity = Integer.MAX_VALUE;
    }

    @Override
    public Iterator<E> iterator() {
        return element.iterator();
    }

    @Override
    public int size() {
        return count.get();
    }

    @Override
    public void put(E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        int c = -1;
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try{

            while (count.get() == capacity) {
                notFull.await();
            }
            boolean enqueue = enqueue(e);
            if(enqueue) {
                c = count.getAndIncrement();
            }else {
                c = count.get();
            }
            if(c+1 < capacity){
                notFull.signal();
            }
        }finally {
            putLock.unlock();
        }
        if(c == 0){
            signalNotEmpty();
        }
    }
    /**
     * Signals a waiting take. Called only from put/offer (which do not
     * otherwise ordinarily lock takeLock.)
     */
    private void signalNotEmpty() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }

    /**
     * Signals a waiting put. Called only from take/poll.
     */
    private void signalNotFull() {
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    /**
     * Links node at end of queue.
     *
     * @param node the node
     */
    private boolean enqueue(E node) {
        return element.add(node);
    }

    /**
     * Removes a node from head of queue.
     *
     * @return the node
     */
    private E dequeue() {
        Optional<E> first = element.stream().findFirst();
        E x = null;
        if(first.isPresent()){
            x = first.get();
            element.remove(x);
        }
        return x;
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException("offer");
    }

    @Override
    public E take() throws InterruptedException {
        E x;
        int c = -1;
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            while (count.get() == 0) {
                notEmpty.await();
            }
            x = dequeue();
            c = count.getAndDecrement();
            if (c > 1) {
                notEmpty.signal();
            }
        } finally {
            takeLock.unlock();
        }
        if (c == capacity) {
            signalNotFull();
        }
        return x;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException("poll");
    }

    @Override
    public int remainingCapacity() {
        throw new UnsupportedOperationException("remainingCapacity");
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        throw new UnsupportedOperationException("drainTo");
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        throw new UnsupportedOperationException("drainTo");
    }

    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException("offer");
    }

    @Override
    public E poll() {
        throw new UnsupportedOperationException("poll");
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException("peek");
    }
}
