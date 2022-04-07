package com.example.project.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

public class Order implements Serializable {
    String userKey ;
    String orderNo ;
    LatLng pickupLocation;
    String locationDetail ;
    String pickUpDate ;
    String pickUpTime ;
    String dropOffDate ;
    String dropOffTime ;
    String service ;
    String status ;

    public Order() {
    }

    public Order(String userKey, String orderNo, LatLng pickupLocation, String locationDetail, String pickUpDate, String pickUpTime, String dropOffDate, String dropOffTime, String service,String status) {
        this.userKey = userKey;
        this.orderNo = orderNo;
        this.pickupLocation = pickupLocation;
        this.locationDetail = locationDetail;
        this.pickUpDate = pickUpDate;
        this.pickUpTime = pickUpTime;
        this.dropOffDate = dropOffDate;
        this.dropOffTime = dropOffTime;
        this.service = service;
        this.status = status ;
    }

    public static int gen() {
        Random r = new Random( System.currentTimeMillis() );
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public LatLng getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(LatLng pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getDropOffDate() {
        return dropOffDate;
    }

    public void setDropOffDate(String dropOffDate) {
        this.dropOffDate = dropOffDate;
    }

    public String getDropOffTime() {
        return dropOffTime;
    }

    public void setDropOffTime(String dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userKey='" + userKey + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", pickupLocation=" + pickupLocation +
                ", locationDetail='" + locationDetail + '\'' +
                ", pickUpDate='" + pickUpDate + '\'' +
                ", pickUpTime='" + pickUpTime + '\'' +
                ", dropOffDate='" + dropOffDate + '\'' +
                ", dropOffTime='" + dropOffTime + '\'' +
                ", service='" + service + '\'' +
                '}';
    }
}
