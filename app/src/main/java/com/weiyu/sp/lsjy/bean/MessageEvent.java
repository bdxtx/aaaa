package com.weiyu.sp.lsjy.bean;

public class MessageEvent {
    private Object message;
    private boolean state;
    private String tag;
    public  MessageEvent(Object message,boolean state,String tag){
        this.message=message;
        this.state=state;
        this.tag=tag;
    }
    public  MessageEvent(Object message,String  tag){
        this.message=message;
        this.tag=tag;
    }
    public MessageEvent(Object message){
        this.message=message;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getTag() {
        return tag == null ? "" : tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
