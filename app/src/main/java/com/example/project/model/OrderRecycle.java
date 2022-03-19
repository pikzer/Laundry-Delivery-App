package com.example.project.model;

public class OrderRecycle {
    String orderNumTV ;
    String service_tv ;
    String pickordropTv ;
    String droporPickTime ;
    String statusNow_tv ;
    int statusImg ;

    public OrderRecycle(String orderNumTV, String service_tv, String pickordropTv, String droporPickTime, String statusNow_tv, int statusImg) {
        this.orderNumTV = orderNumTV;
        this.service_tv = service_tv;
        this.pickordropTv = pickordropTv;
        this.droporPickTime = droporPickTime;
        this.statusNow_tv = statusNow_tv;
        this.statusImg = statusImg;
    }

    public String getOrderNumTV() {
        return orderNumTV;
    }

    public void setOrderNumTV(String orderNumTV) {
        this.orderNumTV = orderNumTV;
    }

    public String getService_tv() {
        return service_tv;
    }

    public void setService_tv(String service_tv) {
        this.service_tv = service_tv;
    }

    public String getPickordropTv() {
        return pickordropTv;
    }

    public void setPickordropTv(String pickordropTv) {
        this.pickordropTv = pickordropTv;
    }

    public String getDroporPickTime() {
        return droporPickTime;
    }

    public void setDroporPickTime(String droporPickTime) {
        this.droporPickTime = droporPickTime;
    }

    public String getStatusNow_tv() {
        return statusNow_tv;
    }

    public void setStatusNow_tv(String statusNow_tv) {
        this.statusNow_tv = statusNow_tv;
    }

    public int getStatusImg() {
        return statusImg;
    }

    public void setStatusImg(int statusImg) {
        this.statusImg = statusImg;
    }
}
