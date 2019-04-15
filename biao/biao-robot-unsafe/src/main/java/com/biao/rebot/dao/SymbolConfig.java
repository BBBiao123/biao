package com.biao.rebot.dao;

import com.biao.rebot.SymbolInfo;
import org.apache.empire.data.DataType;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.apache.empire.db.DBTable;
import org.apache.empire.db.DBTableColumn;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Symbol config.
 *
 */
public class SymbolConfig extends DBTable {
    /**
     * 数据库处理对象；
     */
    private InsolentDBHelper db;

    /**
     * 交易对.
     */
    private final DBTableColumn ID;

    private final DBTableColumn PRICE_SCALE;

    private final DBTableColumn VOLUME_SCALE;

    /**
     * 主交易；
     */
    private final DBTableColumn COIN_MAIN;
    /**
     * 交易对区；
     */
    private final DBTableColumn COIN_OTHER;


    /**
     * Instantiates a new Symbol config.
     *
     * @param db the db
     */
    public SymbolConfig(InsolentDBHelper db) {
        super("js_plat_ex_pair", db);
        this.db = db;
        ID = addColumn("id", DataType.TEXT, 0, true);
        PRICE_SCALE = addColumn("price_precision", DataType.INTEGER, 0, true);
        VOLUME_SCALE = addColumn("volume_precision", DataType.INTEGER, 0, true);
        COIN_MAIN = addColumn("pair_one", DataType.TEXT, 16, true);
        COIN_OTHER = addColumn("pair_other", DataType.TEXT, 16, true);
        setPrimaryKey(ID);
    }

    /**
     * 查询所有配置信息；
     *
     * @return 所有集合信息；
     */
    public Map<String, SymbolInfo> queryBySymbol() {
        DBReader reader = new DBReader();
        Connection connection = this.db.getConnection();
        try {
            DBCommand dbCommand = this.db.createCommand();
            dbCommand.select(ID, PRICE_SCALE, VOLUME_SCALE, COIN_MAIN, COIN_OTHER);
            reader.open(dbCommand, connection);
            SymbolInfo symbol;
            Map<String, SymbolInfo> symbols = new HashMap<>();
            while (reader.moveNext()) {
                symbol = new SymbolInfo();
                symbol.setId(reader.getString(ID));
                symbol.setSymbol(reader.getString(COIN_OTHER)+reader.getString(COIN_MAIN));
                symbol.setPriceScale(reader.getInt(PRICE_SCALE));
                symbol.setVolumeScale(reader.getInt(VOLUME_SCALE));
                symbols.put(symbol.getSymbol(), symbol);
            }
            return symbols;
        } catch (Exception e) {
            throw e;
        } finally {
            reader.close();
            db.close();
        }
    }

}
