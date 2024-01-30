package com.zhangdi.myfunctiontest.excel;

import java.util.Map;

/**
 * author: zhangdi45
 * Date: 14:12 2023/11/2
 */
public class Student {
    private String name;
    private Map<String,String> test;

    private int age;

    public Student() {
    }

    public Student(String name, int age, Map<String, String> test) {
        this.name = name;
        this.test = test;
        this.age = age;
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Map<String, String> getTest() {
        return test;
    }

    public void setTest(Map<String, String> test) {
        this.test = test;
    }

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
}
