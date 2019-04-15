package com.thinkgem.jeesite.modules.plat.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PlatUserCoinCountShow {

    private List<String> dateList;

    private List<String> typeList;

    private Map<String, List<Long>> personMap;

    private Map<String, List<BigDecimal>> coinMap;

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public Map<String, List<Long>> getPersonMap() {
        return personMap;
    }

    public void setPersonMap(Map<String, List<Long>> personMap) {
        this.personMap = personMap;
    }

    public Map<String, List<BigDecimal>> getCoinMap() {
        return coinMap;
    }

    public void setCoinMap(Map<String, List<BigDecimal>> coinMap) {
        this.coinMap = coinMap;
    }
}
