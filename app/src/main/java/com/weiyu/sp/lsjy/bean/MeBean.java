package com.weiyu.sp.lsjy.bean;

public class MeBean {
//    account string
//
//    账户
//
//    coupon string
//
//    优惠劵
//
//    days string
//
//    会员卡剩余天数
//
//    id string
//
//    用户id
//
//    integral string
//
//    积分
//
//    loginName string
//
//    昵称
//
//    url string
//
//    头像
//
//    vip boolean
//
//            是否vip
//
//    vipTypeCode integer($int32)
//
//    vip编码 800教师卡 801大学卡 802学院卡
//
//    vipTypeName string
//
//    vip名称 800教师卡 801大学卡 802学院卡
    private String account;
    private String coupon;
    private String days;
    private String id;
    private String integral;
    private String loginName;
    private String url;
    private String adUrl;
    private boolean vip;
    private String vipTypeCode;
    private String vipTypeName;
    private boolean hasTop;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getVipTypeCode() {
        return vipTypeCode;
    }

    public void setVipTypeCode(String vipTypeCode) {
        this.vipTypeCode = vipTypeCode;
    }

    public String getVipTypeName() {
        return vipTypeName;
    }

    public void setVipTypeName(String vipTypeName) {
        this.vipTypeName = vipTypeName;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public boolean isHasTop() {
        return hasTop;
    }

    public void setHasTop(boolean hasTop) {
        this.hasTop = hasTop;
    }
}
