package com.zhangdi.myfunctiontest.sim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhangdi.myfunctiontest.DividerGridItemDecoration;
import com.zhangdi.myfunctiontest.HomeAdapter;
import com.zhangdi.myfunctiontest.MessageEvent;
import com.zhangdi.myfunctiontest.R;
import com.zhangdi.myfunctiontest.StaggeredHomeAdapter;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SimActivity extends AppCompatActivity {
    private static final String TAG = "SimActivity";
    private Context mContext;
    private List<String> mList;
    private HomeAdapter mHomeAdaper;
    private RecyclerView mRecyclerView;
    private StaggeredHomeAdapter mStaggeredHomeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim);
        mContext = this;
        initData();
        initView();
        EventBus.getDefault().register(this);
    }

    private void initData() {
        mList=new ArrayList<String>();
        mList.add("切换S1m1上网");
        mList.add("切换S1m2上网");
        mList.add("获取网络连接状态");

    }
    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUIMessage(MessageEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBackgroundMessage(MessageEvent event) throws InterruptedException {
        Log.d(TAG, "onBackgroundMessage: " + event.what);
        if (event.what != R.layout.activity_sim){
            return;
        }
        switch (event.arg1){
            case 0:
                Log.d(TAG, "onBackgroundMessage: 切换S1m1上网 = "+SimUtils.getInstance(mContext).changeSimDataBySlot(0));
                break;
            case 1:
                Log.d(TAG, "onBackgroundMessage: 切换S1m2上网 = "+SimUtils.getInstance(mContext).changeSimDataBySlot(1));
                break;
            case 2:
                Log.d(TAG, "onBackgroundMessage: 获取网络连接状态:"+SimUtils.getInstance(mContext).isDataEnabled(10));
                break;

        }
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.sim_recycler);
        Log.d(TAG, "initView: mRecyclerView = "+mRecyclerView);
        //设置GridView
        setGridView();
    }

    public void setGridView(){
        Log.d(TAG, "initView: mRecyclerView = "+mRecyclerView);

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
               EventBus.getDefault().post(new MessageEvent(R.layout.activity_sim,position));
            }

            @Override
            public void onItemLongClick(View view, final int position) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}