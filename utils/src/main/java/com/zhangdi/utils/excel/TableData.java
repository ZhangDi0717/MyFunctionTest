package com.zhangdi.utils.excel;

import java.util.Timer;

public class TableData {
    private int id;
    private String name;
    private String result;
    private String time;


    public TableData(int id, String name, String result, String time) {
        this.id = id;
        this.name = name;
        this.result = result;
        this.time = time;
    }

    public TableData(int id, String name, String result) {
        this.id = id;
        this.name = name;
        this.result = result;
    }

    public TableData() {
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + "--" + result + "\\n" ;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
