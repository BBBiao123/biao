package com.biao.mapper;

import com.biao.entity.TradeRiskControl;
import com.biao.sql.build.TradeRiskControlSqlBuild;
import com.biao.vo.risk.TotalRiskVolume;
import com.biao.vo.risk.TradeRiskVolume;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface TradeRiskControlDao {

    @Select("select " + TradeRiskControlSqlBuild.columns + " from trade_risk_control")
    List<TradeRiskControl> findAll();

    @SelectProvider(type = TradeRiskControlSqlBuild.class, method = "findByUserIdsAndCoinSymbol")
    TradeRiskVolume findByUserIdsAndCoinSymbol(@Param("userIds") String userIds, @Param("coinSymbol") String coinSymbol);

    @SelectProvider(type = TradeRiskControlSqlBuild.class, method = "findTotalRiskVolumeBySymbol")
    TotalRiskVolume findTotalRiskVolumeBySymbol(@Param("coinSymbol") String coinSymbol);


}
