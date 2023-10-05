package com.zhangdi.myfunctiontest;

public class MessageEvent {
    public int action;
    public int what;

    public int arg1;

    public int arg2;

    public Object obj;


    public MessageEvent(int action, int what) {
        this.action = action;
        this.what = what;
    }
}
