package com.biao.mapper;

import com.biao.entity.CoinCollection;
import com.biao.sql.build.CoinCollectionBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * The interface Coin collection dao.
 */
@Mapper
public interface CoinCollectionDao {


    /**
     * Find by id coin collection.
     *
     * @param id the id
     * @return the coin collection
     */
    @SelectProvider(type = CoinCollectionBuild.class, method = "findById")
    CoinCollection findById(String id);

    /**
     * Find by symbol list.
     *
     * @param symbol the symbol
     * @return the list
     */
    @Select("select " + CoinCollectionBuild.columns + " from coin_collection where symbol =#{symbol} and status = 0")
    List<CoinCollection> findBySymbol(String symbol);

    /**
     * Update status by id integer.
     *
     * @param id the id
     * @return the integer
     */
    @Update(" update  coin_collection set status = 1 where id = #{id}")
    Integer updateStatusById(String id);

}
