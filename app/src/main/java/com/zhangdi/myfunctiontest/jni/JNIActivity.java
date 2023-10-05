package com.zhangdi.myfunctiontest.jni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhangdi.myfunctiontest.DividerGridItemDecoration;
import com.zhangdi.myfunctiontest.HomeAdapter;
import com.zhangdi.myfunctiontest.R;
import com.zhangdi.myfunctiontest.StaggeredHomeAdapter;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JNIActivity extends AppCompatActivity {
    private static final String TAG = "JNIActivity";
    private Context mContext;
    private List<String> mList;
    private HomeAdapter mHomeAdaper;
    private RecyclerView mRecyclerView;
    private StaggeredHomeAdapter mStaggeredHomeAdapter;

    static {
        System.loadLibrary("myapplication");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jniactivity);
        mContext = this;
        initData();
        initView();
    }

    private void initData() {
        mList=new ArrayList<String>();
        mList.add("日志");
        mList.add("jni 类映射");
    }

    private void initView() {
        mRecyclerView= (RecyclerView) this.findViewById(R.id.jni_recycler);
        //设置GridView
        setGridView();
    }

    public void setGridView(){
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,
            StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mHomeAdaper=new HomeAdapter(this, mList);
        setLister();
        mRecyclerView.setAdapter(mHomeAdaper);
    }

    private void setLister(){
        mHomeAdaper.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                switch (position){
                    case 0:
                        Log.d(TAG, "onItemClick: stringFromJNI = "+stringFromJNI());
                        logTest();
                        break;
                    case 1:
                        mappingClass();
                        break;
                    case 2:

                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, final int position) {

            }
        });
    }

    public native String stringFromJNI();

    public native void logTest();

    public native void mappingClass();
}