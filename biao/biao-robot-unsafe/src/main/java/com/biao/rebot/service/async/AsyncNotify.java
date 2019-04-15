package com.biao.rebot.service.async;

/**
 * AsyncNotify.
 * <p>
 * 可异步通知的消息格式.
 * <p>
 * 18-12-18下午2:31
 *
 * @param <T> the type parameter
 *  "" sixh
 */
public interface AsyncNotify<T extends AsyncData> {

    /**
     * Notify.
     *
     * @param t the t
     */
    void notify(T t);

    /**
     * 增加一个需要的通知消息体.
     * @param notify notify.
     */
    void addNotify(AsyncNotify<T> notify);

    /**
     * 重置连接.
     */
    void reset();
}
