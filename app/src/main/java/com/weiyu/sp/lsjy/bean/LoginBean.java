package com.weiyu.sp.lsjy.bean;

public class LoginBean {
    private String loginToken;
    private String refreshLoginToken;
    private String uname;
    private String loginName;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getRefreshLoginToken() {
        return refreshLoginToken;
    }

    public void setRefreshLoginToken(String refreshLoginToken) {
        this.refreshLoginToken = refreshLoginToken;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
