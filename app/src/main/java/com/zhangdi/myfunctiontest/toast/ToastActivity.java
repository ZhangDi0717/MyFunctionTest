package com.zhangdi.myfunctiontest.toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhangdi.myfunctiontest.DividerGridItemDecoration;
import com.zhangdi.myfunctiontest.HomeAdapter;
import com.zhangdi.myfunctiontest.MessageEvent;
import com.zhangdi.myfunctiontest.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ToastActivity extends AppCompatActivity {
    private static final String TAG = ToastActivity.class.getSimpleName();

    private Context mContext;
    private List<String> mList;
    private HomeAdapter mHomeAdaper;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);
        mContext = this;
        initData();
        initView();
        EventBus.getDefault().register(this);
    }
    private void initData() {
        mList=new ArrayList<String>();
        mList.add("普通Toast");
        mList.add("取消Toast");

    }
    private void initView() {
        mRecyclerView = findViewById(R.id.toast_recycler);
        Log.d(TAG, "initView: mRecyclerView = "+mRecyclerView);
        //设置GridView
        setGridView();
    }
    private int index = 0;
    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUIMessage(MessageEvent event) {
        if (event.action != R.layout.activity_toast){
            return;
        }
        Log.d(TAG, "onUIMessage: event.what " + event.what);
        switch (event.what){
            case 0:
                ToastUtil.show(mContext,"Toast show:"+index++, Toast.LENGTH_LONG);
                break;
            case 1:
                ToastUtil.cancelToast();
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBackgroundMessage(MessageEvent event) throws InterruptedException {

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
                Log.d(TAG, "onItemClick: position = " + position);
                EventBus.getDefault().post(new MessageEvent(R.layout.activity_toast,position));
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