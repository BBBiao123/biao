package com.biao.vo;

import java.io.Serializable;

/**
 * 修改手机号vo
 *
 *  ""oury
 */
public class MobileVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String oldMobile;

    private String newMobile;

    private String oldCode;

    private String newCode;

    public String getOldMobile() {
        return oldMobile;
    }

    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile;
    }

    public String getNewMobile() {
        return newMobile;
    }

    public void setNewMobile(String newMobile) {
        this.newMobile = newMobile;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

}
