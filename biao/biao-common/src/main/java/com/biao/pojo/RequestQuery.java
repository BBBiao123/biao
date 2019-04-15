package com.biao.pojo;

import java.io.Serializable;

/**
 * 分页请求对象
 */
public class RequestQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private int showCount = 10; // 每页显示记录数
    private int totalResult = 0; // 总记录数
    private int totalPage = 0; // 总页数
    private int currentPage = 1; // 当前页

    /**
     * 查询起始位置
     */
    private int start;

    /**
     * 排序字符串
     */
    private String orderByStr;

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    /**
     * 直接获取起始位置
     *
     * @return
     */
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    /**
     * 通过当前页返回起始位置
     *
     * @return
     */
    public int getPageStart() {
        return (this.getCurrentPage() - 1) * this.showCount;
    }

    public String getOrderByStr() {
        return orderByStr;
    }

    public void setOrderByStr(String orderByStr) {
        this.orderByStr = orderByStr;
    }

    public int getTotalPage() {
        if (totalResult % showCount == 0) {
            totalPage = totalResult / showCount;
        } else {
            totalPage = (totalResult / showCount) + 1;
        }
        return totalPage;
    }

    public int getCurrentPage() {
        if (currentPage <= 0) {
            currentPage = 1;
        }
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
