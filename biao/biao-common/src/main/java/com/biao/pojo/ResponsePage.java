package com.biao.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponsePage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> list = new ArrayList<>();

    private Long count = 0L;

    public ResponsePage() {
    }

    public ResponsePage(List<T> list, Long count) {
        this.list = list;
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
