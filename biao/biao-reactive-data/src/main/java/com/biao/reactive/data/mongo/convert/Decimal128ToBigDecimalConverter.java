package com.biao.reactive.data.mongo.convert;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/4/22 下午5:36
 * @since JDK 1.8
 */
public class Decimal128ToBigDecimalConverter implements Converter<Decimal128, BigDecimal> {

    public BigDecimal convert(Decimal128 decimal128) {
        return decimal128.bigDecimalValue();
    }
}
