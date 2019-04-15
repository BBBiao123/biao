package com.biao.rebot.dao;

import com.biao.rebot.config.RobotParam;
import org.apache.empire.db.DBDatabase;

/**
 * 数据层的管理；
 *
 *
 */
public class RobotSupper extends DBDatabase {

    /**
     * 操作；
     */
    private final static class Holder {
        private static final  RobotSupper INSTANCE = new RobotSupper();
    }

    /**
     * 获取当前对像；
     *
     * @return 对象 ；
     */
    public static RobotSupper get() {
        return Holder.INSTANCE;
    }

    /**
     * 封装一个可以操作的 数据。
     *
     * @return 配置信息；
     */
    private JdbcConfig getJdbcConfig() {
        JdbcConfig config = new JdbcConfig();
        config.setPass(RobotParam.get().getRobotCtx().getJdbcPass());
        config.setUrl(RobotParam.get().getRobotCtx().getJdbcUrl());
        config.setUsername(RobotParam.get().getRobotCtx().getJdbcUser());
        return config;
    }

    /**
     * 获取一个数据库可以操作的Robot对象；
     *
     * @return RobotConfig信息 ；
     */
    public RobotConfig gerRobotConfig() {
        return new RobotConfig(new InsolentDBHelper(getJdbcConfig()));
    }

    /**
     * Get joinSymbol config joinSymbol config.
     *
     * @return the joinSymbol config
     */
    public SymbolConfig getSymbolConfig(){
        return new SymbolConfig(new InsolentDBHelper(getJdbcConfig()));
    }
}
