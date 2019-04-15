package com.biao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *  ""(Myth)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderNoTotalVO {

    private BigDecimal totalVolume;
}
