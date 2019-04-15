package com.thinkgem.jeesite.modules.plat.constant;

/**
 * UserCoinVolumeBillStatusEnum.
 * <p>
 *     bb资产的状态.
 * <p>
 * 19-1-2下午2:27
 *
 * @author chenbin sixh
 */
public enum  UserCoinVolumeBillStatusEnum {

    /**
     * 未处理.
     */
    UNPROCESSED(0),

    /**
     * 处理中.
     */

    PROCESSING(1),

    /**
     * 已处理.
     */
    SUCCESS(2),

    /**
     * 处理失败.
     */
    FAILURE(3),

    /**
     * 需要重试.
     */
    RETRY(4);

    private Integer status;

    UserCoinVolumeBillStatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }}

