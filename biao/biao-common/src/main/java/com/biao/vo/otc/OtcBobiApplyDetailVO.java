package com.biao.vo.otc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtcBobiApplyDetailVO implements Serializable {
    private String groupId;

    private String groupName;

    private String userId;

    private String realName;

    private String mobile;

    private BigDecimal total;

    private BigDecimal rate;

    private String currencyUnit;
}
