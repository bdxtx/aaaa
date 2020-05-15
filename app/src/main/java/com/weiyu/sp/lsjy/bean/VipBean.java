package com.weiyu.sp.lsjy.bean;

import java.util.List;

public class VipBean {
    private List<PayChannel> pay;
    private List<CardType> card;

    public List<PayChannel> getPay() {
        return pay;
    }

    public void setPay(List<PayChannel> pay) {
        this.pay = pay;
    }

    public List<CardType> getCard() {
        return card;
    }

    public void setCard(List<CardType> card) {
        this.card = card;
    }
}
