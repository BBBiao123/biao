package com.biao.wallet.exception;

/**
 * @author: haiqingzheng
 * @since: 2017年11月14日 下午1:05:03
 * @history:
 */
public enum EBizErrorCode {

    ADDRESS_CREATE_ERROR("1000", "地址创建失败"),

    UTXO_INSERT_ERROR("1200", "交易插入失败"), TX_FEE_ERROR("1201", "拉取手续费失败"),

    UTXO_COLLECTION_ERROR("1300", "归集失败"),

    BLOCK_GET_ERROR("1400", "获取区块数据失败"),

    WITHDRAW_COUNT_LESS("1400", "平台提现账户余额不足"),

    DEFAULT("xn802000", "业务异常");
    // PUSH_STATUS_UPDATE_FAILURE("eth000001","地址状态更新失败");

    private String code;

    private String info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    EBizErrorCode(String code, String info) {
        this.code = code;
        this.info = info;
    }
}
