package com.biao.sql;

import com.biao.constant.Constants;
import com.biao.exception.PlatException;
import com.biao.util.CollectionHelp;
import com.biao.util.ReflectionUtil;
import com.biao.util.StringHelp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SqlBuild {

    private static Logger logger = LoggerFactory.getLogger(SqlBuild.class);

    public static final String INSERT_START = "insert into ";

    public static final String UPDATE_START = "update ";

    private static final String JDBC_TYPE = ",jdbcType=";

    public static final String SELECT_START = "select ";

    private static final String ALIAS_SYMBOL = " AS ";

    public static final String MYBATIES_STRING = "VARCHAR";

    public static final String MYBATIES_INTEGER = "INTEGER";

    public static final String MYBATIES_SHORT = "SMALLINT";

    public static final String MYBATIES_BOOLEAN = "BOOLEAN";

    public static final String MYBATIES_DOUBLE = "DOUBLE";

    public static final String MYBATIES_LONG = "BIGINT";

    public static final String MYBATIES_NUMERIC = "NUMERIC";

    public static final String MYBATIES_BIGDECIMAL = "DECIMAL";

    public static final String MYBATIES_DATE = "DATE";

    public static final String MYBATIES_TIME = "TIME";

    public static final String MYBATIES_TIMESTAMP = "TIMESTAMP";

    public static final String MYBATIES_BLOB = "BLOB";

    /**
     * 自动构建插入语句
     *
     * @param clazz
     * @return insert into 表名  (column) values (value)
     */
    public static String buildInsertSql(Class<?> clazz) {
        return buildInsertSql(clazz, 0);
    }

    /**
     * 自动构建插入语句
     *
     * @param clazz
     * @return insert into 表名  (column) values (value)
     */
    public static String buildInsertSql(Class<?> clazz, Object target) {
        return buildInsertSql(clazz, 0);
    }

    /**
     * 自动构建插入语句
     *
     * @param clazz
     * @param type  用于过滤column,通过{@link com.by.common.sql.SqlField}中type的值
     * @return insert into 表名  (column) values (value)
     */
    public static String buildInsertSql(Class<?> clazz, int type) {
        StringBuilder builder = new StringBuilder(INSERT_START);
        SqlTable sqlTable = existSqlTable(clazz);
        //表名
        builder.append(sqlTable.value());
        builder.append(" ");
        builder.append("(");
        //开始构建字段
        String[] columsAndValues = buildInsertColumn(clazz, type);
        if (StringUtils.isBlank(columsAndValues[0])) {
            logger.error("clazz:{},not column insert", clazz);
            throw new PlatException(Constants.GLOBAL_ERROR_CODE, "please config SqlField Annotation");
        }
        builder.append(columsAndValues[0]);
        builder.append(")");
        builder.append(" values ");
        builder.append("(");
        builder.append(columsAndValues[1]);
        builder.append(")");
        return builder.toString();
    }

    public static String buildBatchInsertSql(List<?> values, Class<?> clazz, int type, String paramName) {
        StringBuilder builder = new StringBuilder(INSERT_START);
        SqlTable sqlTable = existSqlTable(clazz);
        //表名
        builder.append(sqlTable.value());
        builder.append(" ");
        builder.append("(");
        String[] columnAndValues = buildBatchInsertColumn(values, clazz, type, paramName);
        builder.append(columnAndValues[0]);
        builder.append(")");
        builder.append(" values ");
        builder.append(columnAndValues[1]);
        return builder.toString();
    }

    /**
     * 采用case when then
     * sql语句： update table set
     * field = case id
     * when id值 then field值
     * when id值 then field值
     * end ,
     * field = case id
     * when id值 then field值
     * end
     * WHERE id IN (ids)
     *
     * @param values
     * @param clazz
     * @param paramName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String buildBatchUpdateSql(List<?> values, Class<?> clazz, String paramName) {
        StringBuilder builder = new StringBuilder(UPDATE_START);
        SqlTable sqlTable = existSqlTable(clazz);
        //表名
        builder.append(sqlTable.value());
        builder.append(" ");
        builder.append("set ");
        Field primaryKeyField = primaryField(clazz);
        SqlField primaryKeySqlField = existSqlField(primaryKeyField);
        Object[] fields = createBatchUpdateFieldsArrays(values, clazz, paramName, primaryKeyField);
        String ids = (String) fields[0];
        StringBuilder updateFieldsArraysBuilder = new StringBuilder();
        Map<String, List<String>> batchUpdateFieldsArrays = (Map<String, List<String>>) fields[1];
        batchUpdateFieldsArrays.forEach((field, batchUpdateFieldsArray) -> {
            updateFieldsArraysBuilder.append(field);
            updateFieldsArraysBuilder.append(" =");
            updateFieldsArraysBuilder.append(" case");
            updateFieldsArraysBuilder.append(" ");
            updateFieldsArraysBuilder.append(primaryKeySqlField.value());
            batchUpdateFieldsArray.forEach(batchUpdateField -> {
                updateFieldsArraysBuilder.append(" " + batchUpdateField);
            });
            updateFieldsArraysBuilder.append(" end,");
        });
        String updateFieldsArraysSql = StringHelp.substring(updateFieldsArraysBuilder.toString());
        builder.append(updateFieldsArraysSql);
        builder.append(" where ");
        builder.append(primaryKeySqlField.value());
        builder.append(" in (");
        builder.append(ids);
        builder.append(")");
        return builder.toString();
    }

    private static Object[] createBatchUpdateFieldsArrays(List<?> values, Class<?> clazz, String paramName, Field primaryKeyField) {
        Object[] sqlFieldArrays = new Object[2];
        StringBuilder idsSqlBuilder = new StringBuilder();
        Map<String, List<String>> batchUpdateFieldsArrays = new HashMap<>();
        Field[] fields = ReflectionUtil.getAllField(clazz);
        for (int i = 0; i < values.size(); i++) {
            Object listValue = values.get(i);
            for (Field field : fields) {
                //主键
                if (isPrimaryKey(field, 0)) {
                    idsSqlBuilder.append("#{");
                    idsSqlBuilder.append(paramName);
                    idsSqlBuilder.append("[");
                    idsSqlBuilder.append(i);
                    idsSqlBuilder.append("]");
                    idsSqlBuilder.append(".");
                    idsSqlBuilder.append(field.getName());
                    idsSqlBuilder.append("},");
                    continue;
                }
                SqlField sqlField = existSqlField(field);
                Object objectValue = ReflectionUtil.getFieldValue(listValue, field.getName());
                if (sqlField == null || objectValue == null) {
                    continue;
                }
                if ((objectValue instanceof String) && StringUtils.isEmpty((String) objectValue)) {
                    continue;
                }
                List<String> whereSqlList = batchUpdateFieldsArrays.get(sqlField.value());
                if (whereSqlList == null) {
                    whereSqlList = new ArrayList<>();
                    batchUpdateFieldsArrays.put(sqlField.value(), whereSqlList);
                }
                //when id值 then field值
                String idConditionSql = "#{" + paramName + "[" + i + "]" + "." + primaryKeyField.getName() + "}";
                String valueConditionSql = "#{" + paramName + "[" + i + "]" + "." + field.getName() + "}";
                whereSqlList.add("when " + idConditionSql + " then " + valueConditionSql);
            }
        }
        sqlFieldArrays[0] = StringHelp.substring(idsSqlBuilder.toString());
        sqlFieldArrays[1] = batchUpdateFieldsArrays;
        return sqlFieldArrays;
    }

    private static String[] buildBatchInsertColumn(List<?> datas, Class<?> clazz, int type, String paramName) {
        String[] columsAndValues = new String[2];
        StringBuilder columns = new StringBuilder("");
        StringBuilder values = new StringBuilder("");
        Field[] fields = ReflectionUtil.getAllField(clazz);
        if (fields != null) {
            List<Field> names = new ArrayList<>();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                //跳过主键
                if (isPrimaryKey(field, 1)) {
                    continue;
                }
                SqlField sqlField = existSqlField(field);
                if (sqlField != null && contain(sqlField, type)) {
                    columns.append(sqlField.value());
                    columns.append(",");
                    names.add(field);
                }
            }
            columsAndValues[0] = StringHelp.substring(columns.toString());
            for (int i = 0; i < datas.size(); i++) {
                values.append("(");
                for (int j = 0; j < names.size(); j++) {
                    Field field = names.get(j);
                    SqlField sqlField = field.getAnnotation(SqlField.class);
                    values.append("#{");
                    values.append(paramName);
                    values.append("[");
                    values.append(i);
                    values.append("]");
                    values.append(".");
                    values.append(field.getName());
                    if (StringUtils.isNotBlank(sqlField.jdbcType())) {
                        values.append(JDBC_TYPE);
                        values.append(sqlField.jdbcType());
                    } else {
                        String jdbcType = autoDiscernment(field);
                        if (logger.isDebugEnabled()) {
                            logger.debug("构建insertSql自动识别属性类型,识别后的jdbcType:{}", jdbcType);
                        }
                        if (StringUtils.isNotBlank(jdbcType)) {
                            values.append(JDBC_TYPE);
                            values.append(jdbcType);
                        }
                    }
                    if (j == names.size() - 1) {
                        values.append("}");
                    } else {
                        values.append("},");
                    }
                }
                values.append("),");
            }
            columsAndValues[1] = StringHelp.substring(values.toString());
        }
        return columsAndValues;
    }

    /**
     * 构建Insert操作colums和values
     *
     * @param clazz
     * @param type
     * @return
     */
    private static String[] buildInsertColumn(Class<?> clazz, int type) {
        String[] columsAndValues = new String[2];
        StringBuilder columns = new StringBuilder("");
        StringBuilder values = new StringBuilder("");
        Field[] fields = ReflectionUtil.getAllField(clazz);
        buildInsertColumn(columns, values, fields, type);
        String sqlColumns = StringHelp.substring(columns.toString());
        String sqlValues = StringHelp.substring(values.toString());
        if (logger.isDebugEnabled()) {
            logger.debug("class:{},自动构建的colums:{},values:{}", clazz.getName(), sqlColumns, sqlValues);
        }
        columsAndValues[0] = sqlColumns;
        columsAndValues[1] = sqlValues;
        return columsAndValues;
    }

    private static void buildInsertColumn(StringBuilder columns, StringBuilder values, Field[] fields, int type) {
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                //跳过主键
                if (isPrimaryKey(field, 1)) {
                    continue;
                }
                SqlField sqlField = existSqlField(field);
                if (sqlField != null && contain(sqlField, type)) {
                    columns.append(sqlField.value());
                    columns.append(",");
                    values.append("#{");
                    values.append(field.getName());
                    if (StringUtils.isNotBlank(sqlField.jdbcType())) {
                        values.append(JDBC_TYPE);
                        values.append(sqlField.jdbcType());
                    } else {
                        String jdbcType = autoDiscernment(field);
                        if (logger.isDebugEnabled()) {
                            logger.debug("构建insertSql自动识别属性类型,识别后的jdbcType:{}", jdbcType);
                        }
                        if (StringUtils.isNotBlank(jdbcType)) {
                            values.append(JDBC_TYPE);
                            values.append(jdbcType);
                        }
                    }
                    values.append("},");
                }
            }
        }
    }

    private static String autoDiscernment(Field field) {
        Class<?> type = field.getType();
        if (type == String.class) {
            return MYBATIES_STRING;
        } else if (type == Date.class) {
            return MYBATIES_TIMESTAMP;
        } else if (type == byte.class) {
            return MYBATIES_SHORT;
        } else if (type == BigDecimal.class) {
            return MYBATIES_NUMERIC;
        } else if (type == Integer.class || type == Integer.TYPE) {
            return MYBATIES_INTEGER;
        } else if (type == Long.class || type == Long.TYPE) {
            return MYBATIES_LONG;
        } else if (type == Double.class || type == Double.TYPE) {
            return MYBATIES_NUMERIC;
        } else if (type == Float.class || type == Float.TYPE) {
            return MYBATIES_NUMERIC;
        } else if (type == Byte[].class || type == byte[].class) {
            return MYBATIES_BLOB;
        } else if (type == Boolean.class || type == Boolean.TYPE) {
            return MYBATIES_BOOLEAN;
        } else if (type == BigDecimal.class) {
            return MYBATIES_BIGDECIMAL;
        } else if (type == Short.class || type == Short.TYPE) {
            return MYBATIES_SHORT;
        } else if (type == int[].class || type == Integer[].class) {
            return MYBATIES_BLOB;
        } else if (type == LocalDate.class) {
            return MYBATIES_DATE;
        } else if (type == LocalDateTime.class) {
            return MYBATIES_TIMESTAMP;
        }
        return null;
    }

    /**
     * 通过对象获取其中存在的值字段
     *
     * @param obj
     * @return
     */
    public static String buildUpdateColums(Object obj) {
        return buildUpdateColums(obj, 0, "");
    }

    /**
     * 通过对象获取其中存在的值字段
     *
     * @param obj
     * @param type
     * @return
     */
    public static String buildUpdateColums(Object obj, int type) {
        return buildUpdateColums(obj, type, "");
    }

    /**
     * 通过对象获取其中存在的值字段
     *
     * @param obj
     * @param type       过滤type
     * @param tableAlias 表别名
     * @return
     */
    public static String buildUpdateColums(Object obj, int type, String tableAlias) {
        StringBuilder builder = new StringBuilder();
        Class<?> clazz = obj.getClass();
        Field[] fields = ReflectionUtil.getAllField(clazz);
        if (fields != null) {
            for (Field field : fields) {
                SqlField sqlField = existSqlField(field);
                if (sqlField == null) {
                    //不存在字段标识跳过
                    continue;
                }
                if (isPrimaryKey(field, 2) || !contain(sqlField, type)) {
                    //主键或者不存在type跳过
                    continue;
                }
                Object value = ReflectionUtil.getFieldValue(obj, field.getName());
                if (value == null && sqlField.updateBreakNull()) {
                    continue;
                }
                if (StringUtils.isNotBlank(tableAlias)) {
                    builder.append(tableAlias);
                    builder.append(".");
                }
                builder.append(sqlField.value());
                builder.append("=");
                builder.append("#{");
                builder.append(field.getName());
                if (value == null) {
                    String jdbcType = autoDiscernment(field);
                    if (StringUtils.isNotBlank(jdbcType)) {
                        builder.append(JDBC_TYPE);
                        builder.append(jdbcType);
                    }
                }
                builder.append("}");
                builder.append(",");
            }
        }
        return StringHelp.substring(builder.toString());
    }

    /**
     * 通过对象获取其中存在的值字段
     *
     * @param obj
     * @param type         过滤type
     * @param tableAlias   表别名
     * @param ignoreFields 忽略的更新sql属性名
     * @return
     */
    public static String buildUpdateColums(Object obj, int type, String tableAlias, String... ignoreFields) {
        StringBuilder builder = new StringBuilder();
        Class<?> clazz = obj.getClass();
        Field[] fields = ReflectionUtil.getAllField(clazz);
        if (fields != null) {
            for (Field field : fields) {
                SqlField sqlField = existSqlField(field);
                if (sqlField == null) {
                    //不存在字段标识跳过
                    continue;
                }
                if (isPrimaryKey(field, 2) || !contain(sqlField, type)) {
                    //主键或者不存在type跳过
                    continue;
                }
                if (ignoreFields != null && ignoreFields.length != 0) {
                    List<String> ignoreLists = CollectionHelp.arrayToList(ignoreFields);
                    if (ignoreLists.contains(field.getName())) {
                        continue;
                    }
                }
                Object value = ReflectionUtil.getFieldValue(obj, field.getName());
                if (value == null && sqlField.updateBreakNull()) {
                    continue;
                }
                if (StringUtils.isNotBlank(tableAlias)) {
                    builder.append(tableAlias);
                    builder.append(".");
                }
                builder.append(sqlField.value());
                builder.append("=");
                builder.append("#{");
                builder.append(field.getName());
                if (value == null) {
                    String jdbcType = autoDiscernment(field);
                    if (StringUtils.isNotBlank(jdbcType)) {
                        builder.append(JDBC_TYPE);
                        builder.append(jdbcType);
                    }
                }
                builder.append("}");
                builder.append(",");
            }
        }
        return StringHelp.substring(builder.toString());
    }

    public static String buildSelectCondition(Object obj, int type, String objectParamName) {
        Class<?> clazz = obj.getClass();
        StringBuilder builder = new StringBuilder("");
        Field[] fields = ReflectionUtil.getAllField(clazz);
        if (fields != null) {
            for (Field field : fields) {
                SqlSelect sqlSelect = existSqlSelect(field);
                if (sqlSelect == null) {
                    //不存在字段标识跳过
                    continue;
                }
                if (!containSelect(sqlSelect, type)) {
                    //不存在type跳过
                    continue;
                }
                SelectType selectType = sqlSelect.selectType();
                if (selectType != SelectType.ISNULL && selectType != SelectType.NOTNULL) {
                    Object value = ReflectionUtil.getFieldValue(obj, field.getName());
                    if (value == null) {
                        continue;
                    }
                }
                builder.append(" and ");
                builder.append(sqlSelect.value());
                switch (selectType) {
                    case EQUAL:
                        builder.append(" = ");
                        builder.append(buildAttrSql(field.getName(), objectParamName));
                        break;
                    case GT:
                        builder.append(" > ");
                        builder.append(buildAttrSql(field.getName(), objectParamName));
                        break;
                    case GE:
                        builder.append(" >= ");
                        builder.append(buildAttrSql(field.getName(), objectParamName));
                        break;
                    case LE:
                        builder.append(" <= ");
                        builder.append(buildAttrSql(field.getName(), objectParamName));
                        break;
                    case LT:
                        builder.append(" < ");
                        builder.append(buildAttrSql(field.getName(), objectParamName));
                        break;
                    case ISNULL:
                        builder.append(" is null");
                        break;
                    case LIKE:
                        builder.append(" ");
                        builder.append(StringHelp.likeStr(field.getName()));
                        break;
                    case NOTEQUAL:
                        builder.append(" != ");
                        builder.append(buildAttrSql(field.getName(), objectParamName));
                        break;
                    case NOTNULL:
                        builder.append(" not null");
                        break;
                    case IN:
                        Collection<?> collect = (Collection<?>) ReflectionUtil.getFieldValue(obj, field.getName());
                        if (!CollectionHelp.isEmpty(collect)) {
                            builder.append(" in (");
                            if (CollectionHelp.isStringCollection(collect)) {
                                builder.append(StringHelp.turnString(CollectionHelp.toListStr(collect)));
                            } else if (CollectionHelp.isIntegerCollection(collect)) {
                                builder.append(StringHelp.turnInt(CollectionHelp.toListInt(collect)));
                            }
                            builder.append(")");
                        }
                        break;
                    default:
                        builder.append("=");
                        builder.append(buildAttrSql(field.getName(), objectParamName));
                        break;
                }
            }
        }
        String sql = builder.toString();
        if (StringUtils.isBlank(sql)) {
            logger.info("obj:{},条件全部为空", obj);
        }
        return sql;
    }

    private static String buildAttrSql(String fieldName, String objectParamName) {
        StringBuilder builder = new StringBuilder();
        builder.append("#{");
        if (StringUtils.isNotBlank(objectParamName)) {
            builder.append(objectParamName);
            builder.append(".");
        }
        builder.append(fieldName);
        builder.append("}");
        return builder.toString();
    }

    /**
     * 自动构建更新语句
     *
     * @param clazz
     * @param conditions 条件添加对象键值
     * @return update 表名 set [column]=[newvalue]  WHERE [column] = [value]
     */
    public static String buildUpdateSql(Class<?> clazz, Map<String, Object> conditions) {
        return buildUpdateSql(clazz, conditions, 0);
    }

    /**
     * 自动构建更新语句
     *
     * @param clazz
     * @param conditions 条件添加对象键值
     * @param type       用于过滤column,通过{@link com.by.common.sql.SqlField}中type的值
     * @return update 表名 set [column]=[newvalue]  WHERE [column] = [value]
     */
    public static String buildUpdateSql(Class<?> clazz, Map<String, Object> conditions, int type) {
        StringBuilder builder = new StringBuilder(UPDATE_START);
        SqlTable sqlTable = existSqlTable(clazz);
        //表名
        builder.append(sqlTable.value());
        builder.append(" SET ");
        //开始构建字段
        Map<String, Object> columsAndValues = buildUpdateColumn(clazz, type);
        if (columsAndValues != null && !columsAndValues.isEmpty()) {
            Set<String> input = columsAndValues.keySet();
            Iterator<String> it = input.iterator();
            StringBuilder data = new StringBuilder();
            for (; it.hasNext(); ) {
                String s = it.next();
                data.append(s + " = " + columsAndValues.get(s));
                if (it.hasNext()) {
                    data.append(",");
                }
            }
            builder.append(data);
        } else {
            logger.error("clazz:{},not column update", clazz);
            throw new PlatException(Constants.GLOBAL_ERROR_CODE, "please config SqlField Annotation");
        }
        if (conditions != null && !conditions.isEmpty()) {
            Set<String> input = conditions.keySet();
            Iterator<String> it = input.iterator();
            StringBuilder condition = new StringBuilder();

            for (; it.hasNext(); ) {
                String s = it.next();
                condition.append(s);
                condition.append(" = ");
                condition.append(conditions.get(s));
                if (it.hasNext()) {
                    condition.append(" AND ");
                }
            }
            builder.append(" WHERE ");
            builder.append(condition);
        }
        return builder.toString();
    }


    /**
     * 自动构建查询语句字段 (配置了别名自动带上别名)
     *
     * @param clazz
     * @return column, column, column, column, column
     */
    public static String buildSelectColumns(Class<?> clazz) {
        return buildSelectColumns(clazz, 0);
    }

    /**
     * 自动构建查询语句字段 (配置了别名自动带上别名)
     *
     * @param clazz
     * @param type
     * @return column, column, column, column, column
     */
    public static String buildSelectColumns(Class<?> clazz, int type) {
        String tableAlias = "";
        SqlTable sqlTable = existSqlTable(clazz);
        if (sqlTable != null) {
            tableAlias = sqlTable.alias();
        }
        return buildSelectColumns(clazz, type, tableAlias);
    }

    /**
     * 自动构建查询语句字段
     *
     * @param clazz
     * @param type
     * @param alias 别名
     * @return
     */
    public static String buildSelectColumns(Class<?> clazz, int type, String alias) {
        String tableAlias = alias;
        StringBuilder columns = new StringBuilder("");
        Field[] fields = ReflectionUtil.getAllField(clazz);
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                SqlField sqlField = existSqlField(field);
                if (sqlField != null && (contain(sqlField, type) || isPrimaryKey(field, 0))) {
                    if (StringUtils.isNotBlank(tableAlias)) {
                        columns.append(tableAlias);
                        columns.append(".");
                    }
                    columns.append(sqlField.value());
                    if (StringUtils.isNotBlank(sqlField.alias())) {
                        columns.append(ALIAS_SYMBOL);
                        columns.append(sqlField.alias());
                    }
                    columns.append(",");
                }
            }
        }
        return StringHelp.substring(columns.toString());
    }

    private static Map<String, Object> buildUpdateColumn(Class<?> clazz, int type) {
        Map<String, Object> columsAndValues = new HashMap<String, Object>();
        Field[] fields = ReflectionUtil.getAllField(clazz);
        buildUpdateColumn(columsAndValues, fields, type);
        return columsAndValues;
    }

    private static void buildUpdateColumn(Map<String, Object> columsAndValues, Field[] fields, int type) {
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                //跳过主键
                if (isPrimaryKey(field, 2)) {
                    continue;
                }
                SqlField sqlField = existSqlField(field);
                if (sqlField != null && contain(sqlField, type)) {
                    columsAndValues.put(sqlField.value(), "#{" + field.getName() + "}");
                }
            }
        }
    }

    /**
     * 获取SqlTable注解
     *
     * @param clazz
     * @return
     */
    public static SqlTable existSqlTable(Class<?> clazz) {
        SqlTable sqlTable = clazz.getAnnotation(SqlTable.class);
        if (sqlTable == null) {
            logger.error("clazz:{},not config SqlTable Annotation", clazz);
            throw new PlatException(Constants.GLOBAL_ERROR_CODE, "please config SqlTable Annotation");
        }
        return sqlTable;
    }

    /**
     * 数组是否包含值
     *
     * @param sqlField
     * @param target
     * @return
     */
    public static boolean contain(SqlField sqlField, int target) {
        if (sqlField == null) {
            return false;
        }
        boolean isContain = false;
        for (int type : sqlField.type()) {
            if (type == target) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    /**
     * 数组是否包含值
     *
     * @param SqlSelect
     * @param target
     * @return
     */
    public static boolean containSelect(SqlSelect sqlSelect, int target) {
        if (sqlSelect == null) {
            return false;
        }
        boolean isContain = false;
        for (int type : sqlSelect.type()) {
            if (type == target) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    /**
     * 获取SqlField注解
     *
     * @param field
     * @return
     */
    public static SqlField existSqlField(Field field) {
        return field.getAnnotation(SqlField.class);
    }

    /**
     * 获取SqlSelectd注解
     *
     * @param field
     * @return
     */
    public static SqlSelect existSqlSelect(Field field) {
        return field.getAnnotation(SqlSelect.class);
    }

    /**
     * 是否有主键
     *
     * @param field
     * @param type  0:其他,1:表示插入,2:表示更新
     * @return true为主键, false不是
     */
    public static boolean isPrimaryKey(Field field, int type) {
        PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
        if (primaryKey == null) {
            return false;
        }
        //更新是否跳过主键
        if (type == 2) {
            return primaryKey.updateIsSkip();
        }
        //插入是否跳过主键
        if (type == 1) {
            return primaryKey.insertIsSkip();
        }
        return true;
    }

    public static PrimaryKey getPrimaryKey(Field field) {
        return field.getAnnotation(PrimaryKey.class);
    }

    public static Field primaryField(Class<?> clazz) {
        Field[] fields = ReflectionUtil.getAllField(clazz);
        Field primaryFeild = null;
        for (Field field : fields) {
            if (isPrimaryKey(field, 0)) {
                primaryFeild = field;
                break;
            }
        }
        if (primaryFeild == null) {
            logger.error("clazz:{},not config PrimaryKey Annotation", clazz);
            throw new PlatException(Constants.GLOBAL_ERROR_CODE, "please config PrimaryKey Annotation");
        }
        return primaryFeild;
    }
}
