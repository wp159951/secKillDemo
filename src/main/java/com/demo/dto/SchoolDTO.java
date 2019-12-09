package com.demo.dto;

/**
 * Created with IntelliJ IDEA
 *
 * @author: yangzhizhuang
 * @date: 2019/12/7
 * @description:
 */
public class SchoolDTO {

    private int reqest_type;

    private int page;

    private int size;

    public int getReqest_type() {
        return reqest_type;
    }

    public void setReqest_type(int reqest_type) {
        this.reqest_type = reqest_type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public SchoolDTO(int reqest_type, int page, int size) {
        this.reqest_type = reqest_type;
        this.page = page;
        this.size = size;
    }

    public SchoolDTO() {
    }
}
