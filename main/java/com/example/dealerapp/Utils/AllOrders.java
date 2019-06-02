package com.example.dealerapp.Utils;

public class AllOrders {

    private String x_point, y_point;

    public AllOrders(String x_point, String y_point) {
        this.x_point = x_point;
        this.y_point = y_point;
    }
    public AllOrders(){

    }

    public String getX_point() {
        return x_point;
    }

    public void setX_point(String x_point) {
        this.x_point = x_point;
    }

    public String getY_point() {
        return y_point;
    }

    public void setY_point(String y_point) {
        this.y_point = y_point;
    }
}
