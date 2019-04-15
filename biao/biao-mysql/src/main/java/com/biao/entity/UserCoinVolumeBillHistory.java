package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.math.BigDecimal;

/**
 * UserCoinVolumeBill.
 * <p>
 *    用户bb资产操作审请.
 * <p>
 * 19-1-2上午11:47
 *
 *  "" sixh
 */
@SqlTable("js_plat_user_coin_volume_bill_history")
@Data
public class UserCoinVolumeBillHistory extends BaseEntity {

    /**
     * 用户id;
     */
    @SqlField("user_id")
    private String userId;

    /**
     * 币总信息.
     */
    @SqlField("coin_symbol")
    private String coinSymbol;

    /**
     * 优先级，5为默认. 数字越大，优先级越高.
     */
    @SqlField("priority")
    private Integer priority;

    /**
     * 外部调用，自行传入的信息。
     */
    @SqlField("ref_key")
    private String refKey;

    /**
     * 需要操作的指令.
     * {@link com.biao.enums.UserCoinVolumeEventEnum}
     */
    @SqlField("op_sign")
    private Integer opSign;

    /**
     * 需要处理的数量.
     */
    @SqlField("op_volume")
    private BigDecimal opVolume;

    /**
     * 需要处理的锁定数量.
     */
    @SqlField("op_lock_volume")
    private BigDecimal opLockVolume;

    /**
     * 数据来源.
     */
    @SqlField("source")
    private String source;

    /**
     * 外部系统的标注.
     */
    @SqlField("mark")
    private String mark;

    /**
     * 处理的状态.
     * {@link com.biao.enums.UserCoinVolumeBillStatusEnum}
     */
    @SqlField("status")
    private Integer status;

    /**
     * 确定是哪台hash服务器拉取
     */
    @SqlField("hash")
    private Integer hash;

    @SqlField("retry_count")
    private Integer retryCount;

    @SqlField("force_lock")
    private Boolean forceLock;
}


