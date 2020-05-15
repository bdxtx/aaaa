package com.weiyu.sp.lsjy.bean;

public class CardType {
//    "vipTypeCode": 800,
//            "vipTypeName": "教师卡",
//            "maxPrice": "100.00",
//            "minPrice": "8000.00",
//            "discount": "800.0"
    private String vipTypeCode;
    private String vipTypeName;
    private String maxPrice;
    private String minPrice;
    private String discount;

    private boolean isSelect;

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

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
