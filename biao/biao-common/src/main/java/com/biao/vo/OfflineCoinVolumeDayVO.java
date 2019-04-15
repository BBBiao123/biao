package com.biao.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class OfflineCoinVolumeDayVO implements Serializable {

    private String symbols;
    private LocalDate startTime;
    private LocalDate endTime;
}
