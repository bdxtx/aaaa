package com.weiyu.sp.lsjy.bean;

public class PayResultBean {
    private int amount;
    private String orderId;
    private String alibody;
    private WxPayBean wxbody;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAlibody() {
        return alibody;
    }

    public void setAlibody(String alibody) {
        this.alibody = alibody;
    }

    public WxPayBean getWxbody() {
        return wxbody;
    }

    public void setWxbody(WxPayBean wxbody) {
        this.wxbody = wxbody;
    }
}
