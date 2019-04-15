package com.biao.sql;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public abstract class BaseSqlBuild<T, ID> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public String insert(T t) {
        String insertSql = SqlBuild.buildInsertSql(this.getEntityClass());
        if (logger.isDebugEnabled()) {
            logger.debug("insert build sql:{}", insertSql);
        }
        return insertSql;
    }

    @SuppressWarnings({"unchecked"})
    public String batchInsert(Map<String, Object> params) {
        List<T> values = (List<T>) params.get("listValues");
        return SqlBuild.buildBatchInsertSql(values, this.getEntityClass(), 0, "listValues");
    }

    @SuppressWarnings({"unchecked"})
    public String batchInsertAndType(Map<String, Object> params) {
        List<T> values = (List<T>) params.get("listValues");
        Integer type = (Integer) params.get("type");
        return SqlBuild.buildBatchInsertSql(values, this.getEntityClass(), type, "listValues");
    }

    public String findById(ID id) {
        StringBuilder builder = new StringBuilder();
        String selectSql = defaultBuildSelectSql();
        builder.append(selectSql);
        builder.append(this.findIdCondition());
        if (logger.isDebugEnabled()) {
            logger.debug("findById build sql:{}", builder.toString());
        }
        return builder.toString();
    }

    public String updateById(T t) {
        StringBuilder builder = new StringBuilder();
        builder.append("update");
        builder.append(" ");
        builder.append(this.findTable());
        builder.append(" set");
        builder.append(" ");
        builder.append(SqlBuild.buildUpdateColums(t));
        builder.append(this.findIdCondition());
        if (logger.isDebugEnabled()) {
            logger.debug("update build sql:{}", builder.toString());
        }
        return builder.toString();
    }

    @SuppressWarnings({"unchecked"})
    public String batchUpdate(Map<String, Object> params) {
        List<T> values = (List<T>) params.get("listValues");
        return SqlBuild.buildBatchUpdateSql(values, this.getEntityClass(), "listValues");
    }

    public String findByObject(Object obj) {
        StringBuilder builder = new StringBuilder();
        String selectSql = defaultBuildSelectSql();
        builder.append(selectSql);
        builder.append(" where 1=1");
        builder.append(SqlBuild.buildSelectCondition(obj, 0, ""));
        builder.append(" order by " + findIdName() + " desc");
        if (logger.isDebugEnabled()) {
            logger.debug("findByObject build sql:{}", builder.toString());
        }
        return builder.toString();
    }

    public String findByObjectAndType(Map<String, Object> params) {
        Object obj = params.get("obj");
        Integer type = (Integer) params.get("type");
        StringBuilder builder = new StringBuilder();
        String selectSql = buildSelectSql(type);
        builder.append(StringUtils.isBlank(selectSql) ? defaultBuildSelectSql() : selectSql);
        builder.append(" where 1=1");
        builder.append(SqlBuild.buildSelectCondition(obj, type, "obj"));
        builder.append(" order by " + findIdName() + " desc");
        if (logger.isDebugEnabled()) {
            logger.debug("findByObjectAndType build sql:{}", builder.toString());
        }
        return builder.toString();
    }

    protected Class<?> getEntityClass() {
        // 泛型转换
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<?> entity = (Class<?>) pt.getActualTypeArguments()[0];
        return entity;
    }

    protected Class<?> getIdClass() {
        // 泛型转换
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<?> id = (Class<?>) pt.getActualTypeArguments()[1];
        return id;
    }

    protected String defaultBuildSelectSql() {
        return buildSelectSql(0);
    }

    protected String buildSelectSql(int type) {
        Class<?> clazz = getEntityClass();
        StringBuilder builder = new StringBuilder(SqlBuild.SELECT_START);
        String colums = SqlBuild.buildSelectColumns(clazz, type);
        builder.append(colums);
        builder.append(" from");
        builder.append(" ");
        builder.append(SqlBuild.existSqlTable(clazz).value());
        return builder.toString();
    }

    protected String buildAllSelectColumn() {
        Class<?> clazz = getEntityClass();
        return SqlBuild.buildSelectColumns(clazz, 0);
    }

    protected String buildSelectColumn(int type) {
        Class<?> clazz = getEntityClass();
        return SqlBuild.buildSelectColumns(clazz, type);
    }

    protected String findTable() {
        Class<?> clazz = getEntityClass();
        return SqlBuild.existSqlTable(clazz).value();
    }

    protected String findIdCondition() {
        StringBuilder builder = new StringBuilder(" where");
        Class<?> clazz = getEntityClass();
        Field field = SqlBuild.primaryField(clazz);
        builder.append(" ");
        builder.append(SqlBuild.existSqlField(field).value());
        builder.append(" = ");
        builder.append("#{");
        builder.append(field.getName());
        builder.append("}");
        return builder.toString();
    }

    protected String findIdName() {
        Class<?> clazz = getEntityClass();
        Field field = SqlBuild.primaryField(clazz);
        return field.getName();
    }

    protected String findAllColums() {
        Class<?> clazz = getEntityClass();
        return SqlBuild.buildSelectColumns(clazz);
    }
}
