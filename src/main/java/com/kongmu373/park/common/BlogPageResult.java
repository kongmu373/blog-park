package com.kongmu373.park.common;

import com.kongmu373.park.entity.BlogVo;

import java.util.List;

public class BlogPageResult extends CommonResult<List<BlogVo>> {
    private int total;
    private int page;
    private int totalPage;

    public static BlogPageResult success(String msg, List<BlogVo> data, int total, int page, int totalPage) {
        return new BlogPageResult(SUCCESS, msg, data, total, page, totalPage);
    }

    public static BlogPageResult fail(String msg, int total, int page, int totalPage) {
        return new BlogPageResult(SUCCESS, msg, null, total, page, totalPage);
    }


    protected BlogPageResult(String status, String msg, List<BlogVo> data, int total, int page, int totalPage) {
        super(status, msg, data);
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
