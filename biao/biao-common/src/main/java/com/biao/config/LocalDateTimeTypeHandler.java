package com.biao.config;


import com.biao.util.DateUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.time.LocalDateTime;


@MappedJdbcTypes(JdbcType.TIMESTAMP)
@MappedTypes(LocalDateTime.class)
public class LocalDateTimeTypeHandler implements TypeHandler<LocalDateTime> {

    @Override
    public void setParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter == null) {
            ps.setNull(i, Types.TIMESTAMP);
        } else {
            LocalDateTime dateTime = DateUtils.toUTCDateTime(parameter);
            ps.setTimestamp(i, Timestamp.valueOf(dateTime));
        }
    }

    @Override
    public LocalDateTime getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp timestamp = cs.getTimestamp(columnIndex);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    @Override
    public LocalDateTime getResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnIndex);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    @Override
    public LocalDateTime getResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}



