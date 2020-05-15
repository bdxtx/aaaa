package com.weiyu.sp.lsjy.bean;

public class BannerBean {
//    "title": "标题一",
////            "inquire": 121,
////            "content": "标题一",
////            "picture": "https://lck2020.hubenwl.com/hbwl/banner/8.png",
////            "hrefLink": "https://www.baidu.com/",
////            "bannerType": 100,
////            "bannerTypeName": "首页",
////            "iconUrl": "",
////            "created": ""
    private String title;
    private int inquire;
    private String content;
    private String picture;
    private String hrefLink;
    private int bannerType;
    private String bannerTypeName;
    private String iconUrl;
    private String created;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getInquire() {
        return inquire;
    }

    public void setInquire(int inquire) {
        this.inquire = inquire;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getHrefLink() {
        return hrefLink;
    }

    public void setHrefLink(String hrefLink) {
        this.hrefLink = hrefLink;
    }

    public int getBannerType() {
        return bannerType;
    }

    public void setBannerType(int bannerType) {
        this.bannerType = bannerType;
    }

    public String getBannerTypeName() {
        return bannerTypeName;
    }

    public void setBannerTypeName(String bannerTypeName) {
        this.bannerTypeName = bannerTypeName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
