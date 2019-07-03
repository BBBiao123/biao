package com.bbex.robot.dao;

import com.bbex.robot.RobotPamart;
import org.apache.empire.db.DBDatabase;

/**
 * 数据层的管理；
 *
 * @author p
 */
public class RobotSupper extends DBDatabase {

    /**
     * 操作；
     */
    private final static class Holder {
        private final static RobotSupper INSTNACE = new RobotSupper();
    }

    /**
     * 获取当前对像；
     *
     * @return 对象；
     */
    public static RobotSupper get() {
        return Holder.INSTNACE;
    }

    /**
     * 数据库操作类；
     */
    private InsolentDBHelper db = new InsolentDBHelper(getJdbcConfig());

    /**
     * 封装一个可以操作的 数据。
     *
     * @return 配置信息；
     */
    private JdbcConfig getJdbcConfig() {
        JdbcConfig config = new JdbcConfig();
        config.setPass(RobotPamart.get().getRobotCtx().getJdbcPass());
        config.setUrl(RobotPamart.get().getRobotCtx().getJdbcUrl());
        config.setUsername(RobotPamart.get().getRobotCtx().getJdbcUser());
        return config;
    }

    /**
     * 获取一个数据库可以操作的Robot对象；
     *
     * @return RobotConfig信息；
     */
    public RobotConfig gerRobotConfig() {
        return new RobotConfig(db);
    }
}
