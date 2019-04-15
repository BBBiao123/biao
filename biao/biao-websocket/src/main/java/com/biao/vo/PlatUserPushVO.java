package com.biao.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 推送给平台用户的数据对象.
 *
 *  ""(Myth)
 */
@Data
public class PlatUserPushVO implements Serializable {

    /**
     * 推送给用户的挂单信息.
     */
    private OrderVo orderVo;

    /**
     * 用户主交易区资产信息.
     */
    private UserCoinVolumeVO coinMainVolume;

    /**
     * 用户得到的币资产信息.
     */
    private UserCoinVolumeVO coinOtherVolume;


}
