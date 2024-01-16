package com.zhangdi.myfunctiontest;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.zhangdi.myfunctiontest.sound.SoundPlayer;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * author: zhangdi45
 * Date: 13:57 2023/11/2
 */
public abstract class BaseAppcompat extends AppCompatActivity {
    private Context mContext;
    private HomeAdapter mHomeAdaper;
    private RecyclerView mRecyclerView;
    private StaggeredHomeAdapter mStaggeredHomeAdapter;


    protected List<String> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        mContext = this;
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        mList=new ArrayList<String>();
        initData();
        initView();
    }

    protected abstract void initData();
    protected abstract void onItemClicked(View view,int position);


    private void initView() {
        mRecyclerView= (RecyclerView) this.findViewById(R.id.recycler);
        //设置GridView
        setGridView();
        //设置ListView
//        setListView();
        //设置瀑布流
//        setWaterfallView();
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
                onItemClicked(view,position);
            }

            @Override
            public void onItemLongClick(View view, final int position) {

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
