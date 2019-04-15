package com.biao.rebot.dao;

import com.biao.enums.TradeEnum;
import com.biao.rebot.config.Login;
import com.biao.rebot.config.RobotWeight;
import org.apache.empire.data.DataType;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.apache.empire.db.DBTable;
import org.apache.empire.db.DBTableColumn;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据的配置 信息；
 *
 *
 */
public class RobotConfig extends DBTable {

    /**
     * 数据库处理对象；
     */
    private InsolentDBHelper db;

    /**
     * 主键
     */
    private final DBTableColumn ID;
    /**
     * 类型；
     */
    private final DBTableColumn TYPE;
    /**
     * 主交易；
     */
    private final DBTableColumn COIN_MAIN;
    /**
     * 交易对区；
     */
    private final DBTableColumn COIN_OTHER;
    /**
     * 用户id;
     */
    private final DBTableColumn USER_ID;
    /**
     * 数量区域
     */
    private final DBTableColumn VOLUME_RANGE;
    /**
     * 价格区域
     */
    private final DBTableColumn PRICE_RANGE;
    /**
     * 状态；
     */
    private final DBTableColumn STATUS;

    private final DBTableColumn IS_INIT;
    private final DBTableColumn USER_NAME;
    private final DBTableColumn PASSWORD;

    public RobotConfig(InsolentDBHelper db) {
        super("robot_config_unsafe", db);
        this.db = db;
        ID = addColumn("id", DataType.TEXT, 0, true);
        TYPE = addColumn("type", DataType.INTEGER, 0, true);
        COIN_MAIN = addColumn("coin_main", DataType.TEXT, 16, true);
        COIN_OTHER = addColumn("coin_other", DataType.TEXT, 16, true);
        USER_ID = addColumn("user_id", DataType.TEXT, 64, true);
        VOLUME_RANGE = addColumn("volume_range", DataType.TEXT, 100, true);
        PRICE_RANGE = addColumn("price_range", DataType.TEXT, 100, true);
        USER_NAME = addColumn("user_name", DataType.TEXT, 100, true);
        PASSWORD = addColumn("password", DataType.TEXT, 100, true);
        STATUS = addColumn("status", DataType.INTEGER, 0, true);
        IS_INIT = addColumn("is_init", DataType.INTEGER, 0, true);
        setPrimaryKey(ID);
    }

    /**
     * 修改初始化状态
     * @param init yes
     */
    public void updateStatus(boolean init){
        try {
            DBCommand command = db.createCommand();
            Connection connection = this.db.getConnection();
            command.set(IS_INIT.to(init? 1: 0));
            db.executeUpdate(command,connection);
        }catch (Exception ex){
            throw ex;
        }finally {
//            record.close();
            db.close();
        }
    }
    /**
     * 查询所有配置信息；
     *
     * @return 所有集合信息；
     */
    public List<RobotWeight> queryAll() {
        DBReader reader = new DBReader();
        Connection connection = this.db.getConnection();
        try {
            DBCommand dbCommand = this.db.createCommand();
            dbCommand.select(ID, TYPE, COIN_MAIN, COIN_OTHER, USER_ID, VOLUME_RANGE, PRICE_RANGE, IS_INIT, USER_NAME, PASSWORD);
            dbCommand.where(STATUS.is(0));
            reader.open(dbCommand, connection);
            RobotWeight weight;
            List<RobotWeight> robotWeights = new ArrayList<>();
            while (reader.moveNext()) {
                Login login = new Login();
                weight = new RobotWeight();
                weight.setCoinMain(reader.getString(COIN_MAIN));
                weight.setCoinOther(reader.getString(COIN_OTHER));
                weight.setPriceRange(reader.getString(PRICE_RANGE));
                weight.setVolumeRange(reader.getString(VOLUME_RANGE));
                weight.setTradeEnum(TradeEnum.valueOf(reader.getInt(TYPE)));
                weight.setInit(reader.getInt(IS_INIT) == 1);
                login.setUserId(reader.getString(USER_ID));
                login.setUserName(reader.getString(USER_NAME));
                login.setPassword(reader.getString(PASSWORD));
                weight.setLogin(login);
                robotWeights.add(weight);
            }
            return robotWeights;
        } catch (Exception e) {
            throw e;
        } finally {
            reader.close();
            db.close();
        }
    }
}

