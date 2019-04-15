package com.biao.reactive.data.mongo.convert;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/4/22 下午5:33
 * @since JDK 1.8
 */
public class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {

    public Decimal128 convert(BigDecimal bigDecimal) {
        return new Decimal128(bigDecimal.setScale(16, BigDecimal.ROUND_HALF_UP));
    }

}
