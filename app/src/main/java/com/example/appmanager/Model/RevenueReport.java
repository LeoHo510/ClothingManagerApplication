package com.example.appmanager.Model;

public class RevenueReport {
    private float RevenueSales;
    private int month;

    public float getTotal_price() {
        return RevenueSales;
    }

    public void setTotal_price(float total_price) {
        RevenueSales = total_price;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
