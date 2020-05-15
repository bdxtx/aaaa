package com.weiyu.sp.lsjy.bean;

public class WxPayBean {

//    appId	:	wx8483a828cccbb532
//    nonceStr	:	smM8hbMXXBGQE5eMowKp76kqx9wugTTZ
//    packageValue	:	Sign=WXPay
//    partnerId	:	1556973001
//    paySign	:	B444BAF2737ECEA6F60CB79610F1F3DE
//    prepayId	:	wx081030480062429bcd4d0c901217990400
//    signType	:	MD5
//    timeStamp	:	1588905048

    private String appid;
    private String noncestr;
    private String packageValue;
    private String paySign;
    private String signtype;
    private String prepayid;
    private String timestamp;
    private String partnerid;

    public String getAppId() {
        return appid;
    }

    public void setAppId(String appid) {
        this.appid = appid;
    }

    public String getNonceStr() {
        return noncestr;
    }

    public void setNonceStr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getSignType() {
        return signtype;
    }

    public void setSignType(String signType) {
        this.signtype = signType;
    }

    public String getPrepayId() {
        return prepayid;
    }

    public void setPrepayId(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPartnerId() {
        return partnerid;
    }

    public void setPartnerId(String partnerid) {
        this.partnerid = partnerid;
    }
}
