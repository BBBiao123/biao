package com.biao.mapper;

import com.biao.entity.SuperBook;
import com.biao.sql.build.SuperBookSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SuperBookDao {
    @InsertProvider(type = SuperBookSqlBuild.class, method = "insert")
    void insert(SuperBook superBook);

    @InsertProvider(type = SuperBookSqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<SuperBook> superBookList);

    @Select("SELECT " + SuperBookSqlBuild.columns + " FROM js_plat_super_book t WHERE t.user_id = #{userId} AND t.symbol = #{symbol} LIMIT 1 ")
    SuperBook findByUserIdAndSymbol(@Param("userId") String userId, @Param("symbol") String symbol);
}
