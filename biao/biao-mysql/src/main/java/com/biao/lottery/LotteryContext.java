package com.biao.lottery;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  ""(Myth)
 */
@Getter
@Setter
@NoArgsConstructor
public class LotteryContext {

    /**
     * 中奖数字范围起点（通常0作为起点）
     */
    private Integer winningStartCode;
    /**
     * 当前概率计算出的中奖数字范围终点
     */
    private Integer winningEndCode;

    /**
     * 中奖的数字范围
     */
    private Integer codeScope;


}
