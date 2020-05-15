package com.weiyu.sp.lsjy.bean;

public class BalanceBean {
    private String totalMoney;
    private String frozenMoney;
    private String withdrawableMoney;
    private String addAccountMoney;

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getWithdrawableMoney() {
        return withdrawableMoney;
    }

    public void setWithdrawableMoney(String withdrawableMoney) {
        this.withdrawableMoney = withdrawableMoney;
    }

    public String getAddAccountMoney() {
        return addAccountMoney;
    }

    public void setAddAccountMoney(String addAccountMoney) {
        this.addAccountMoney = addAccountMoney;
    }

    public String getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(String frozenMoney) {
        this.frozenMoney = frozenMoney;
    }
}
