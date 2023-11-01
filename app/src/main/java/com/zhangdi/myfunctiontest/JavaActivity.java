package com.zhangdi.myfunctiontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class JavaActivity extends AppCompatActivity {
    private static final String TAG = "JavaActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        int a = 1;
        Log.d(TAG, "onCreate: a = " + a);
        function1(a);
        Log.d(TAG, "onCreate: a = " + a);

        List list = new ArrayList<>();
        function1(list);
        Log.d(TAG, "onCreate: list.size() = "+list.size());
    }

    private void function1(int a) {
        a = a+1;
    }

    private void function1(List<String> list) {
        list.add("hello");
    }
}