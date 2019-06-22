package com.bbex.robot.dao;

import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBDatabaseDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库帮助类；
 *
 * @author p
 */
public class InsolentDBHelper extends DBDatabase implements Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsolentDBHelper.class);
    /**
     * 连接；
     */
    private Connection connection;

    /**
     * @param config config;
     */
    public InsolentDBHelper(JdbcConfig config) {
        try {
            init(config);
        } catch (RuntimeException e) {
            LOGGER.error("DBHelper:init mysql database error....{}", e);
            close();
            throw e;
        }
    }

    /**
     * 初始化；不会调用连接池；
     *
     * @param config 配置信息；
     * @throws RuntimeException 如果连接错误
     */
    public void init(JdbcConfig config) throws RuntimeException {
        try {
            DBDatabaseDriver driver = (DBDatabaseDriver) Class.forName("org.apache.empire.db.mysql.DBDatabaseDriverMySQL").newInstance();
            this.driver = driver;
            connection = getJdbcConnection("com.mysql.jdbc.Driver",
                    config.getUrl(),
                    config.getUsername(),
                    config.getPass());
        } catch (Exception e) {
            throw new RuntimeException("DBHelper:connection mysql database error:" + e);
        }
    }


    /**
     * 获取一个连接；
     *
     * @return 返回一个连接；
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * 获取jdbc
     *
     * @param jdbcClass class
     * @param url       url
     * @param user      user
     * @param pwd       pwd
     * @return 返回一个连接；
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static Connection getJdbcConnection(String jdbcClass, String url, String user, String pwd) throws SQLException, ClassNotFoundException {
        try {
            Class.forName(jdbcClass);
            return DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 关闭连接
     */
    @Override
    public void close() {
        if (connection != null) {
            try {
                super.close(connection);
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("DBHelper:close connection mysql database error:....,{}", e);
            }
        }
    }

    /**
     * commit一个连接；
     */

    public void commit() {
        this.commit(connection);
    }
}
