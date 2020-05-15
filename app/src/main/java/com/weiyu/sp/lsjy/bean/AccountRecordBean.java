package com.weiyu.sp.lsjy.bean;

public class AccountRecordBean {
//    "titleName": "购买VIP",
//            "successName": "操作中",
//            "amount": "+8.00",
//            "bsDesc": "好友消费",
//            "created": "04-17 23:17"

    private String titleName;
    private String successName;
    private String amount;
    private String bsDesc;
    private String created;

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getSuccessName() {
        return successName;
    }

    public void setSuccessName(String successName) {
        this.successName = successName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBsDesc() {
        return bsDesc;
    }

    public void setBsDesc(String bsDesc) {
        this.bsDesc = bsDesc;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
