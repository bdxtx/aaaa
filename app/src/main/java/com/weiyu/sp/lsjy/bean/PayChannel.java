package com.weiyu.sp.lsjy.bean;

public class PayChannel {
//            "paymentCode": 3000,
//                    "paymentName": "支付宝",
//                    "paymentUrl": "https://lsjy2020.oss-cn-hangzhou.aliyuncs.com/get/2.jpg"
    private String paymentCode;
    private String paymentName;
    private String paymentUrl;
    private boolean select;

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
