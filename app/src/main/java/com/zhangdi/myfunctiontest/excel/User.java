package com.zhangdi.myfunctiontest.excel;

public class User {
    private String name;
    private int age;
    private String[] likes;
    // 省略 get 与 set 方法


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String[] getLikes() {
        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }
}
