package com.biao.rebot.common;

import org.apache.commons.lang3.StringUtils;

/**
 * 返射获取一个对象；
 *
 *
 */
public class RobotInstanceReflect {

    /**
     * 获取一个反射对象；
     *
     * @param classPath classPath;
     * @return path;
     */
    public <T> T reflect(String classPath) {

        if (StringUtils.isBlank(classPath)) {
            throw new NullPointerException("class path is null");
        }
        try {
            return (T) Class.forName(classPath).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
